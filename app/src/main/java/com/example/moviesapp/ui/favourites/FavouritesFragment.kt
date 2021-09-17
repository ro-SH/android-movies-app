package com.example.moviesapp.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {

    private val TAG = "FavouritesFragment"

    private lateinit var favouritesViewModel: FavouritesViewModel
    private var _binding: FragmentFavouritesBinding? = null

    private val favouritesAdapter = FavouritesAdapter(
        FavouritesAdapter.OnClickListener {
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
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.rv_divider)!!)
        binding.fragmentFavouritesRvFavourites.apply {
            adapter = favouritesAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(dividerItemDecoration)
        }
    }
}
