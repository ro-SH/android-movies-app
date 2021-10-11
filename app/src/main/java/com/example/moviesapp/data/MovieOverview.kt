package com.example.moviesapp.data

import com.example.moviesapp.data.source.database.DatabaseMovieOverview
import com.google.gson.annotations.SerializedName

// Latest string
const val LATEST = "latest"

// Popular string
const val POPULAR = "popular"

// Now Playing string
const val NOW_PLAYING = "now playing"

// Top Rated string
const val TOP_RATED = "top rated"

/**
 * Data class for Movie object received from TMDB.
 * @property page page number
 * @property total_pages total pages number
 * @property results list of movies
 */
data class MovieObject(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<MovieOverview>
)

/**
 * Data class for Movie overview.
 * @property id movie id
 * @property title English movie title
 * @property poster_path poster path or null
 * @property release_date
 * @property vote_average average movie score
 */
data class MovieOverview(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("vote_average") val vote_average: Float
)

/**
 * Converts list of MovieOverview to array of DatabaseMovieOverview for saving in database.
 * @param newCategory movies category
 * @return array of DatabaseMovieOverview
 */
fun List<MovieOverview>.asDatabaseModel(newCategory: String): Array<DatabaseMovieOverview> {
    return map {
        DatabaseMovieOverview(
            id = it.id,
            title = it.title,
            poster_path = it.poster_path,
            release_date = it.release_date,
            vote_average = it.vote_average,
            category = newCategory
        )
    }.toTypedArray()
}
