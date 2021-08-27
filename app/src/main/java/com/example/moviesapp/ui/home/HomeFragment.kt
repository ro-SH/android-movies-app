package com.example.moviesapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val topRatedAdapter = MoviesOverviewAdapter(
        MoviesOverviewAdapter.OnClickListener {
            homeViewModel.displayMovieDetails(it.id)
        }
    )

    private val popularAdapter = MoviesOverviewAdapter(
        MoviesOverviewAdapter.OnClickListener {
            homeViewModel.displayMovieDetails(it.id)
        }
    )

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()

        homeViewModel.topRated.observe(
            viewLifecycleOwner,
            { movies ->
                topRatedAdapter.setData(movies)
            }
        )

        homeViewModel.popular.observe(
            viewLifecycleOwner,
            { movies ->
                popularAdapter.setData(movies)
            }
        )

        homeViewModel.navigateToSelectedMovie.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    Log.i(TAG, "id: $it")
                    this.findNavController()
                        .navigate(HomeFragmentDirections.actionShowMovieDetails(it))
                    homeViewModel.displayMovieDetailsComplete()
                }
            }
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        // Top Rated RecyclerView
        binding.fragmentHomeRvTopRated.adapter = topRatedAdapter
        binding.fragmentHomeRvTopRated.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Popular Recycler View
        binding.fragmentHomeRvPopular.adapter = popularAdapter
        binding.fragmentHomeRvPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}
