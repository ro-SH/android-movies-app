package com.example.moviesapp.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.data.MovieOverview
import com.example.moviesapp.data.source.MoviesCategories
import com.example.moviesapp.data.source.MoviesRepository
import com.example.moviesapp.data.source.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : ViewModel() {
    val TAG = "HomeViewModel"

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

    private val _loaded = MutableLiveData<Boolean>()
    val loaded: LiveData<Boolean>
        get() = _loaded

    init {
        _loaded.value = false
        getCategories()
    }

    private fun getCategories() {
        var count = 0
        coroutineScope.launch {
            for (category in MoviesCategories.values()) {
                Log.i(TAG, "$category")
                if (repository.getCategory(category)) {
                    count++
                    Log.i(TAG, "+")
                }
            }
            Log.i(TAG, "success count: $count")
            if (count == MoviesCategories.values().size) _loaded.value = true
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
