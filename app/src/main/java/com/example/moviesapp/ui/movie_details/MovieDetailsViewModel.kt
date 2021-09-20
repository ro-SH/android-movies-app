package com.example.moviesapp.ui.movie_details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.source.MoviesRepository
import com.example.moviesapp.data.source.database.MoviesDatabase
import com.example.moviesapp.data.source.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailsViewModel(application: Application, private val movieId: Int) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database: MoviesDatabase = getDatabase(application)
    private val repository: MoviesRepository = MoviesRepository(database)

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?>
        get() = _movie

    private val _favourite = MutableLiveData<Boolean>()
    val favourite: LiveData<Boolean>
        get() = _favourite

    private val _resultReady = MutableLiveData<Boolean>()
    val resultReady: LiveData<Boolean>
        get() = _resultReady

    init {
        _resultReady.value = false
        setFavourite()
        getMovieDetails()
    }

    private fun setFavourite() {
        coroutineScope.launch {
            try {
                _favourite.value = repository.isFavourite(movieId)
            } catch (t: Throwable) {
                _favourite.value = false
            }
        }
    }

    fun getMovieDetails() {
        coroutineScope.launch {
            _resultReady.value = false
            _movie.value = repository.getMovie(movieId)
            _resultReady.value = true
        }
    }

    fun changeFavourite() {
        _favourite.value = !_favourite.value!!
    }

    fun saveFavouriteState() {
        coroutineScope.launch {
            if (favourite.value!!) {
                _movie.value?.let { repository.addToFavourites(it) }
            } else {
                _movie.value?.let { repository.deleteFromFavourites(it) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
