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
        val viewModelFactory = MovieDetailsViewModelFactory(movieId)

        movieDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(MovieDetailsViewModel::class.java)

        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        movieDetailsViewModel.movie.observe(
            viewLifecycleOwner,
            {
                it?.let { movie ->
                    binding.fragmentMovieDetailsTvTagline.text = movie.tagline
                    binding.fragmentMovieDetailsTvOriginalTitle.text = getOriginalTitleFromMovie(movie)
                    binding.fragmentMovieDetailsTvInfo.text = getInfoFromMovie(movie)
                    binding.fragmentMovieDetailsTvRunTime.text = getRunTimeFromMovie(movie)
                    binding.fragmentMovieDetailsTvAverage.text = getAverageFromMovie(movie)
                    binding.fragmentMovieDetailsTvOverview.text = movie.overview

                    val imgUrl = IMAGE_BASE_URL + movie.poster_path
                    val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                    Glide.with(binding.fragmentMovieDetailsIvPoster.context)
                        .load(imgUri)
                        .into(binding.fragmentMovieDetailsIvPoster)

                    (activity as ToolbarTitleListener).updateTitle(movie.title)
                }
            }
        )

        movieDetailsViewModel.favourite.observe(
            viewLifecycleOwner,
            { favourite ->
                if (mMenuItem != null) {
                    when (favourite) {
                        true -> mMenuItem!!.setIcon(R.drawable.round_star_24)
                        else -> mMenuItem!!.setIcon(R.drawable.round_star_border_24)
                    }
                }
            }
        )

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_details_overflow_menu, menu)
        mMenuItem = menu[0]
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.favourite -> movieDetailsViewModel.changeFavourite()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
