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
import com.example.moviesapp.ui.adapters.MoviesOverviewAdapter

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val latestAdapter = MoviesOverviewAdapter(
        MoviesOverviewAdapter.OnClickListener {
            homeViewModel.displayMovieDetails(it.id)
        }
    )

    private val nowPlayingAdapter = MoviesOverviewAdapter(
        MoviesOverviewAdapter.OnClickListener {
            homeViewModel.displayMovieDetails(it.id)
        }
    )

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val viewModelFactory = HomeViewModelFactory(requireActivity().application)
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.latest.observe(
            viewLifecycleOwner,
            { movies ->
                latestAdapter.setData(movies)
            }
        )

        homeViewModel.nowPlaying.observe(
            viewLifecycleOwner,
            { movies ->
                nowPlayingAdapter.setData(movies)
            }
        )

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

        homeViewModel.loaded.observe(
            viewLifecycleOwner,
            {
                binding.fragmentHomePbLoading.visibility = if (it) View.GONE else View.VISIBLE
                binding.fragmentHomeTvPopular.visibility = if (!it) View.GONE else View.VISIBLE
                binding.fragmentHomeTvTopRated.visibility = if (!it) View.GONE else View.VISIBLE
                binding.fragmentHomeTvLatest.visibility = if (!it) View.GONE else View.VISIBLE
                binding.fragmentHomeTvNowPlaying.visibility = if (!it) View.GONE else View.VISIBLE
                binding.fragmentHomeRvPopular.visibility = if (!it) View.GONE else View.VISIBLE
                binding.fragmentHomeRvTopRated.visibility = if (!it) View.GONE else View.VISIBLE
                binding.fragmentHomeRvLatest.visibility = if (!it) View.GONE else View.VISIBLE
                binding.fragmentHomeRvNowPlaying.visibility = if (!it) View.GONE else View.VISIBLE
            }
        )
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

        // Latest Recycler View
        binding.fragmentHomeRvLatest.adapter = latestAdapter
        binding.fragmentHomeRvLatest.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Now Playing Recycler View
        binding.fragmentHomeRvNowPlaying.adapter = nowPlayingAdapter
        binding.fragmentHomeRvNowPlaying.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}
