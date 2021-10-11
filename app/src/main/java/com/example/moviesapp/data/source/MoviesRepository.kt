package com.example.moviesapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.moviesapp.data.*
import com.example.moviesapp.data.source.database.MoviesDatabase
import com.example.moviesapp.data.source.database.asDomainModel
import com.example.moviesapp.data.source.network.MoviesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Enum class of Movies categories.
 */
enum class MoviesCategories {
    LATEST, NOW_PLAYING, TOP_RATED, POPULAR
}

// MoviesCategories converted to string
private val movieCategoryToString = mapOf(
    MoviesCategories.LATEST to LATEST,
    MoviesCategories.NOW_PLAYING to NOW_PLAYING,
    MoviesCategories.TOP_RATED to TOP_RATED,
    MoviesCategories.POPULAR to POPULAR
)

/**
 * Movies repository for interacting with TMDB Api and Database.
 * @property database instance of MoviesDatabase
 */
class MoviesRepository(private val database: MoviesDatabase) {

    // LiveData of List of Upcoming movies from [database]
    val latest: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getMovieOverviews(LATEST)
    ) {
        it.asDomainModel()
    }

    // LiveData of List of Now Playing movies from [database]
    val nowPlaying: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getMovieOverviews(NOW_PLAYING)
    ) {
        it.asDomainModel()
    }

    // LiveData of List of Popular movies from [database]
    val popular: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getMovieOverviews(POPULAR)
    ) {
        it.asDomainModel()
    }

    // LiveData of List of Top Rated movies from [database]
    val topRated: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getMovieOverviews(TOP_RATED)
    ) {
        it.asDomainModel()
    }

    // LiveData of List of Favourite movies from [database]
    val favourites: LiveData<List<MovieOverview>> = Transformations.map(
        database.moviesDao.getFavourites()
    ) {
        it.asDomainModel()
    }

    /**
     * Get movies from TMDB by query [search].
     * @param search query for seach
     * @return List of found movies or null
     */
    suspend fun searchMovies(search: String): List<MovieOverview>? {
        var movies: List<MovieOverview>? = null
        withContext(Dispatchers.IO) {
            try {
                movies = MoviesApi.retrofitService.searchMovies(search).results
            } catch (t: Throwable) {}
        }

        return movies
    }

    /**
     * Get movie from TMDB by [id].
     * @param id movie id
     * @return movie or null
     */
    suspend fun getMovie(id: Int): Movie? {
        var movie: Movie? = null
        withContext(Dispatchers.IO) {
            try {
                movie = MoviesApi.retrofitService.getMovie(id)
            } catch (t: Throwable) {}
        }

        return movie
    }

    /**
     * Add movie to favourites.
     * @param movie
     */
    suspend fun addToFavourites(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.moviesDao.insertMovie(movie.asDatabaseModel())
        }
    }

    /**
     * Returns if movie by [movieId] is in [database].
     * @param movieId
     * @return true if it is [database] else false
     */
    suspend fun isFavourite(movieId: Int): Boolean {
        var result = false
        withContext(Dispatchers.IO) {
            val movieFromDb = database.moviesDao.getMovie(movieId)
            if (movieFromDb != null) result = true
        }

        return result
    }

    /**
     * Delete [movie] from favourites in [database].
     * @param movie
     */
    suspend fun deleteFromFavourites(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.moviesDao.deleteMovie(movie.id)
        }
    }

    /**
     * Get all of categories from TMDB and inserting movies to [database]
     * @return true if success else null
     */
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
