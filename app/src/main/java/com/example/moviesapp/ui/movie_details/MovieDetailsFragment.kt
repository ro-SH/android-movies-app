package com.example.moviesapp.ui.movie_details

import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moviesapp.*
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.source.network.IMAGE_BASE_URL
import com.example.moviesapp.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private var _binding: FragmentMovieDetailsBinding? = null

    private val binding get() = _binding!!

    private var mMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val movieId = MovieDetailsFragmentArgs.fromBundle(requireArguments()).movieId
        val viewModelFactory = MovieDetailsViewModelFactory(requireActivity().application, movieId)

        movieDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(MovieDetailsViewModel::class.java)

        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        (activity as ToolbarTitleListener).updateTitle("")
        (activity as BottomNavListener).updateBottomNavVisibility(false)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentMovieDetailsSrlLayout.setOnRefreshListener {
            movieDetailsViewModel.getMovieDetails()
        }

        movieDetailsViewModel.favourite.observe(
            viewLifecycleOwner,
            {
                if (mMenuItem != null) {
                    when (it) {
                        true -> mMenuItem!!.setIcon(R.drawable.round_star_24)
                        else -> mMenuItem!!.setIcon(R.drawable.round_star_border_24)
                    }
                }
            }
        )

        movieDetailsViewModel.resultReady.observe(
            viewLifecycleOwner,
            {
                setupVisibility(movieDetailsViewModel.movie.value, it)
            }
        )

        movieDetailsViewModel.movie.observe(viewLifecycleOwner, { setupInfo(it) })
    }

    private fun setupInfo(it: Movie?) {
        it?.let { movie ->
            binding.fragmentMovieDetailsTvTitle.text = movie.title
            binding.fragmentMovieDetailsTvTagline.text = getTaglineFromMovie(movie)
            binding.fragmentMovieDetailsTvOriginalTitle.text = getOriginalTitleFromMovie(movie)
            binding.fragmentMovieDetailsTvGenres.text = getGenresFromMovie(movie)
            binding.fragmentMovieDetailsTvRunTime.text = getRunTimeFromMovie(movie)
            binding.fragmentMovieDetailsTvOverview.text = movie.overview
            binding.fragmentMovieDetailsTvVotes.text = getVotesFromMovie(movie)

            binding.fragmentMovieDetailsTvScore.apply {
                text = movie.vote_average.toString()
                setBackgroundColor(getScoreBackground(movie.vote_average))
            }

            binding.fragmentMovieDetailsIvPoster.apply {
                val imgUrl = IMAGE_BASE_URL + movie.poster_path
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                Glide.with(this.context)
                    .load(imgUri)
                    .override(500, 500)
                    .placeholder(R.drawable.loading_placeholder)
                    .fitCenter()
                    .into(this)
            }

            (activity as ToolbarTitleListener).updateTitle(movie.title)
        }
    }

    private fun setupVisibility(it: Movie?, ready: Boolean) {
        binding.fragmentMovieDetailsIvPoster.visibility =
            if (it != null && ready) View.VISIBLE else View.GONE
        binding.fragmentMovieDetailsTvTagline.visibility =
            if (it != null && ready) View.VISIBLE else View.GONE
        binding.fragmentMovieDetailsTvOriginalTitle.visibility =
            if (it != null && ready) View.VISIBLE else View.GONE
        binding.fragmentMovieDetailsTvGenres.visibility =
            if (it != null && ready) View.VISIBLE else View.GONE
        binding.fragmentMovieDetailsTvRunTime.visibility =
            if (it != null && ready) View.VISIBLE else View.GONE
        binding.fragmentMovieDetailsCvScore.visibility =
            if (it != null && ready) View.VISIBLE else View.GONE
        binding.fragmentMovieDetailsTvOverview.visibility =
            if (it != null && ready) View.VISIBLE else View.GONE

        binding.fragmentMovieDetailsTvOverviewTitle.visibility =
            if (it != null && ready) View.VISIBLE else View.GONE

        binding.fragmentMovieDetailsTvNoConnection.visibility =
            if (it == null && ready) View.VISIBLE else View.GONE

        binding.fragmentMovieDetailsSrlLayout.isRefreshing = !ready
//        binding.fragmentMovieDetailsPbLoading.visibility =
//            if (!ready) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_details_overflow_menu, menu)
        mMenuItem = menu[0]
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.favourite -> {
                movieDetailsViewModel.changeFavourite()
                saveFavouriteState()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveFavouriteState() {
        movieDetailsViewModel.saveFavouriteState()
    }
}
