package com.example.moviesapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.moviesapp.data.*
import com.example.moviesapp.data.source.database.MoviesDatabase
import com.example.moviesapp.data.source.database.asDomainModel
import com.example.moviesapp.data.source.network.MoviesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class MoviesCategories {
    LATEST, NOW_PLAYING, TOP_RATED, POPULAR
}

private val movieCategoryToString = mapOf(
    MoviesCategories.LATEST to LATEST,
    MoviesCategories.NOW_PLAYING to NOW_PLAYING,
    MoviesCategories.TOP_RATED to TOP_RATED,
    MoviesCategories.POPULAR to POPULAR
)

class MoviesRepository(private val database: MoviesDatabase) {

    val latest: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getMovieOverviews(LATEST)
    ) {
        it.asDomainModel()
    }

    val nowPlaying: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getMovieOverviews(NOW_PLAYING)
    ) {
        it.asDomainModel()
    }

    val popular: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getMovieOverviews(POPULAR)
    ) {
        it.asDomainModel()
    }

    val topRated: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getMovieOverviews(TOP_RATED)
    ) {
        it.asDomainModel()
    }

    val favourites: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getFavourites()
    ) {
        it.asDomainModel()
    }

    suspend fun searchMovies(search: String): List<MovieOverview>? {
        var movies: List<MovieOverview>? = null
        withContext(Dispatchers.IO) {
            try {
                movies = MoviesApi.retrofitService.searchMovies(search).results
            } catch (t: Throwable) {}
        }

        return movies
    }

    suspend fun getMovie(id: Int): Movie? {
        var movie: Movie? = null
        withContext(Dispatchers.IO) {
            try {
                movie = MoviesApi.retrofitService.getMovie(id)
            } catch (t: Throwable) {}
        }

        return movie
    }

    suspend fun addToFavourites(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.moviesDao.insertMovie(movie.asDatabaseModel())
        }
    }

    suspend fun isFavourite(movieId: Int): Boolean {
        var result = false
        withContext(Dispatchers.IO) {
            val movieFromDb = database.moviesDao.getMovie(movieId)
            if (movieFromDb != null) result = true
        }

        return result
    }

    suspend fun deleteFromFavourites(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.moviesDao.deleteMovie(movie.id)
        }
    }

    suspend fun refreshCategories(): Boolean? {

        return withContext(Dispatchers.IO) {
            var success: Boolean? = true
            for (category in MoviesCategories.values()) {
                try {
                    val movies = when (category) {
                        MoviesCategories.LATEST -> MoviesApi.retrofitService.getLatest().results
                        MoviesCategories.NOW_PLAYING -> MoviesApi.retrofitService.getNowPlaying().results
                        MoviesCategories.TOP_RATED -> MoviesApi.retrofitService.getTopRated().results
                        MoviesCategories.POPULAR -> MoviesApi.retrofitService.getPopular().results
                    }

                    if (movies.isNotEmpty()) {
                        database.moviesDao.clearMovieOverviews(movieCategoryToString[category]!!)
                        database.moviesDao.insertMovieOverviews(
                            *movies.asDatabaseModel(movieCategoryToString[category]!!)
                        )
                    }
                } catch (t: Throwable) {
                    success = null
                }
            }

            return@withContext success
        }
    }
}
