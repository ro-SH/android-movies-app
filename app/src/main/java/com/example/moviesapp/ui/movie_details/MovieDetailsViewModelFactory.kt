package com.example.moviesapp.ui.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieDetailsViewModelFactory(
    private val movieId: Int,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            return MovieDetailsViewModel(movieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}