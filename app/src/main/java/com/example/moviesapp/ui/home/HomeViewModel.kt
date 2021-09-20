package com.example.moviesapp.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.data.MovieOverview
import com.example.moviesapp.data.source.MoviesRepository
import com.example.moviesapp.data.source.database.getDatabase
import kotlinx.coroutines.*

class HomeViewModel(application: Application) : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val repository: MoviesRepository = MoviesRepository(database)

    private val _navigateToSelectedMovie = MutableLiveData<Int?>()
    val navigateToSelectedMovie: LiveData<Int?>
        get() = _navigateToSelectedMovie

    val latest: LiveData<List<MovieOverview>> = repository.latest

    val nowPlaying: LiveData<List<MovieOverview>> = repository.nowPlaying

    val popular: LiveData<List<MovieOverview>> = repository.popular

    val topRated: LiveData<List<MovieOverview>> = repository.topRated

    private val _resultReady = MutableLiveData<Boolean?>()
    val resultReady: LiveData<Boolean?>
        get() = _resultReady

    init {
        _resultReady.value = false
        setupCategories()
    }

    private fun setupCategories() {
        if (latest.value.isNullOrEmpty() || nowPlaying.value.isNullOrEmpty() ||
            popular.value.isNullOrEmpty() || topRated.value.isNullOrEmpty()
        ) {
            refreshCategories()
        } else {
            _resultReady.value = true
        }
    }

    fun refreshCategories() {
        coroutineScope.launch {
            _resultReady.value = false
            _resultReady.value = repository.refreshCategories()
        }
    }

    fun displayMovieDetails(movieId: Int) {
        _navigateToSelectedMovie.value = movieId
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
