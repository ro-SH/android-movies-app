package com.example.moviesapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.BottomNavListener
import com.example.moviesapp.databinding.FragmentSearchBinding
import com.example.moviesapp.hideKeyboard
import com.example.moviesapp.ui.adapters.DefaultItemDecorator
import com.example.moviesapp.ui.adapters.MoviesAdapter

class SearchFragment : Fragment() {

    private val moviesAdapter = MoviesAdapter(
        MoviesAdapter.OnClickListener {
            hideKeyboard()
            searchViewModel.displayMovieDetails(it.id)
        }
    )

    private var _binding: FragmentSearchBinding? = null

    private lateinit var searchViewModel: SearchViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val viewModelFactory = SearchViewModelFactory(requireActivity().application)
        searchViewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        (activity as BottomNavListener).updateBottomNavVisibility()

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.result.observe(
            viewLifecycleOwner,
            {
                it?.let { it1 -> moviesAdapter.setData(it1) }
            }
        )

        searchViewModel.resultReady.observe(
            viewLifecycleOwner,
            {
                binding.fragmentSearchRvResult.visibility = when (it) {
                    true -> View.VISIBLE
                    else -> View.GONE
                }

                binding.fragmentSearchPbLoading.visibility = when (it) {
                    false -> View.VISIBLE
                    else -> View.GONE
                }
            }
        )

        searchViewModel.resultEmpty.observe(
            viewLifecycleOwner,
            {
                binding.fragmentSearchTvEmptyResult.visibility = when (it) {
                    true -> View.VISIBLE
                    else -> View.GONE
                }
            }
        )

        searchViewModel.resultNoConnection.observe(
            viewLifecycleOwner,
            {
                binding.fragmentSearchTvNoConnection.visibility = when (it) {
                    true -> View.VISIBLE
                    else -> View.GONE
                }
            }
        )

        binding.fragmentSearchEtSearch.doOnTextChanged { text, _, _, _ ->
            searchViewModel.searchMovies(text.toString())
            if (binding.fragmentSearchRvResult.adapter?.itemCount != 0)
                binding.fragmentSearchRvResult.scrollToPosition(0)
        }

        binding.fragmentSearchEtSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

        searchViewModel.navigateToSelectedMovie.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    findNavController().navigate(
                        SearchFragmentDirections.actionShowMovieDetails(it)
                    )
                    searchViewModel.displayMovieDetailsComplete()
                }
            }
        )
    }

    private fun setupRecyclerView() {
        binding.fragmentSearchRvResult.apply {
            adapter = moviesAdapter
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
