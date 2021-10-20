package com.example.moviesapp.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.BottomNavListener
import com.example.moviesapp.databinding.FragmentFavouritesBinding
import com.example.moviesapp.ui.adapters.DefaultItemDecorator
import com.example.moviesapp.ui.adapters.MoviesAdapter

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private var _binding: FragmentFavouritesBinding? = null

    private val favouritesAdapter = MoviesAdapter(
        MoviesAdapter.OnClickListener {
            favouritesViewModel.displayMovieDetails(it.id)
        }
    )

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        val viewModelFactory = FavouritesViewModelFactory(requireActivity().application)
        favouritesViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavouritesViewModel::class.java)

        (activity as BottomNavListener).updateBottomNavVisibility()

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesViewModel.navigateToSelectedMovie.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    findNavController()
                        .navigate(FavouritesFragmentDirections.actionShowMovieDetails(it))
                    favouritesViewModel.displayMovieDetailsComplete()
                }
            }
        )

        favouritesViewModel.favourites.observe(
            viewLifecycleOwner,
            {
                favouritesAdapter.setData(it)
            }
        )
    }

    private fun setupRecyclerView() {
        binding.fragmentFavouritesRvFavourites.apply {
            adapter = favouritesAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DefaultItemDecorator(36, 24))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
