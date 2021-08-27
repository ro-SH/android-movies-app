package com.example.moviesapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.data.MovieOverview
import com.example.moviesapp.data.source.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository: MoviesRepository = MoviesRepository()

    private val _navigateToSelectedMovie = MutableLiveData<Int?>()
    val navigateToSelectedMovie: LiveData<Int?>
        get() = _navigateToSelectedMovie

    private val _topRated = MutableLiveData<List<MovieOverview>>()
    val topRated: LiveData<List<MovieOverview>>
        get() = _topRated

    private val _popular = MutableLiveData<List<MovieOverview>>()
    val popular: LiveData<List<MovieOverview>>
        get() = _popular

    init {
        getCategories()
        Log.i("HomeViewModel", "count: ${topRated.value?.size}")
    }

    private fun getCategories() {
        coroutineScope.launch {
            try {
                val topRatedResult = repository.getTopRated()
                val popularResult = repository.getPopular()
                if (topRatedResult.isNotEmpty() && popularResult.isNotEmpty()) {
                    _topRated.value = topRatedResult
                    _popular.value = popularResult
                }
            } catch (t: Throwable) {
                _topRated.value = ArrayList()
                _popular.value = ArrayList()
            }
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
