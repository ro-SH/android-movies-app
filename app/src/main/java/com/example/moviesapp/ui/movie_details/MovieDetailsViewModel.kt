package com.example.moviesapp.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.source.MoviesRepository
import kotlinx.coroutines.*

class MovieDetailsViewModel(private val movieId: Int) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository: MoviesRepository = MoviesRepository()

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?>
        get() = _movie

    private val _favourite = MutableLiveData<Boolean>()
    val favourite: LiveData<Boolean>
        get() = _favourite

    init {
        getMovieDetails()
        _favourite.value = false
    }

    private fun getMovieDetails() {
        coroutineScope.launch {
            try {
                _movie.value = repository.getMovie(movieId.toString())
            } catch (t: Throwable) {
                _movie.value = null
            }
        }
    }

    fun changeFavourite() {
        _favourite.value = !_favourite.value!!
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
