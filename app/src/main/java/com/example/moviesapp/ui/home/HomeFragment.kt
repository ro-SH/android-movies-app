package com.example.moviesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.ui.adapters.MoviesOverviewAdapter
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

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

        binding.fragmentHomeSrlLayout.setOnRefreshListener {
            homeViewModel.refreshCategories()
            binding.fragmentHomeSrlLayout.isRefreshing = false
        }

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
                    this.findNavController()
                        .navigate(HomeFragmentDirections.actionShowMovieDetails(it))
                    homeViewModel.displayMovieDetailsComplete()
                }
            }
        )

        homeViewModel.resultReady.observe(
            viewLifecycleOwner,
            {
                setupVisibility(it)
            }
        )
    }

    private fun setupVisibility(result: Boolean?) {
        val moviesFilled = checkMovies()

        binding.fragmentHomeSrlLayout.isRefreshing = result == false

        binding.fragmentHomeTvPopular.visibility =
            if (result != false && moviesFilled) View.VISIBLE else View.GONE
        binding.fragmentHomeTvTopRated.visibility =
            if (result != false && moviesFilled) View.VISIBLE else View.GONE
        binding.fragmentHomeTvLatest.visibility =
            if (result != false && moviesFilled) View.VISIBLE else View.GONE
        binding.fragmentHomeTvNowPlaying.visibility =
            if (result != false && moviesFilled) View.VISIBLE else View.GONE
        binding.fragmentHomeRvPopular.visibility =
            if (result != false && moviesFilled) View.VISIBLE else View.GONE
        binding.fragmentHomeRvTopRated.visibility =
            if (result != false && moviesFilled) View.VISIBLE else View.GONE
        binding.fragmentHomeRvLatest.visibility =
            if (result != false && moviesFilled) View.VISIBLE else View.GONE
        binding.fragmentHomeRvNowPlaying.visibility =
            if (result != false && moviesFilled) View.VISIBLE else View.GONE

        binding.fragmentHomeTvNoConnection.visibility =
            if (result == null && !moviesFilled) View.VISIBLE else View.GONE

        if (result == null && moviesFilled) {
            Snackbar.make(
                binding.fragmentHomeSrlLayout,
                "No Internet connection.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkMovies() = !homeViewModel.popular.value.isNullOrEmpty() ||
        !homeViewModel.topRated.value.isNullOrEmpty() ||
        !homeViewModel.nowPlaying.value.isNullOrEmpty() ||
        !homeViewModel.latest.value.isNullOrEmpty()

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
