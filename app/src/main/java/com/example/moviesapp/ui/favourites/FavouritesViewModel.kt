package com.example.moviesapp.ui.favourites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.data.source.MoviesRepository
import com.example.moviesapp.data.source.database.getDatabase

class FavouritesViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = MoviesRepository(database)

    val favourites = repository.favourites

    private val _navigateToSelectedMovie = MutableLiveData<Int?>()
    val navigateToSelectedMovie: LiveData<Int?>
        get() = _navigateToSelectedMovie

    fun displayMovieDetails(movieId: Int) {
        _navigateToSelectedMovie.value = movieId
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }
}
