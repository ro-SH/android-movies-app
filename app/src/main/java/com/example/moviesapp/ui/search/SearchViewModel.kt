package com.example.moviesapp.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.data.MovieOverview
import com.example.moviesapp.data.source.MoviesRepository
import com.example.moviesapp.data.source.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = MoviesRepository(database)

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _result = MutableLiveData<List<MovieOverview>?>()
    val result: LiveData<List<MovieOverview>?>
        get() = _result

    private val _resultReady = MutableLiveData<Boolean?>()
    val resultReady: LiveData<Boolean?>
        get() = _resultReady

    private val _resultEmpty = MutableLiveData<Boolean>()
    val resultEmpty: LiveData<Boolean>
        get() = _resultEmpty

    private val _resultNoConnection = MutableLiveData<Boolean>()
    val resultNoConnection: LiveData<Boolean>
        get() = _resultNoConnection

    private val _navigateToSelectedMovie = MutableLiveData<Int?>()
    val navigateToSelectedMovie: LiveData<Int?>
        get() = _navigateToSelectedMovie

    init {
        _resultReady.value = null
        _resultEmpty.value = false
        _resultNoConnection.value = false
    }

    fun searchMovies(search: String) {
        if (search.isNotEmpty()) {
            coroutineScope.launch {
                _resultReady.value = false
                _resultEmpty.value = false
                _resultNoConnection.value = false
                _result.value = repository.searchMovies(search)
                when {
                    _result.value == null -> {
                        _resultReady.value = null
                        _resultEmpty.value = false
                        _resultNoConnection.value = true
                    }
                    _result.value!!.isEmpty() -> {
                        _resultReady.value = null
                        _resultEmpty.value = true
                        _resultNoConnection.value = false
                    }
                    else -> {
                        _resultReady.value = true
                        _resultEmpty.value = false
                        _resultNoConnection.value = false
                    }
                }
            }
        } else {
            _resultReady.value = null
            _resultEmpty.value = false
            _resultNoConnection.value = false
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
