package com.example.moviesapp.data.source.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesapp.data.MovieOverview

/**
 * Data class for storing favourite movie in Database.
 *
 * @property id movie id from TMDB
 * @property title English movie title
 * @property poster_path poster path, nullable
 * @property release_date date string
 * @property vote_average average movie score
 */
@Entity
data class DatabaseFavourite constructor(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster_path: String?,
    val release_date: String,
    val vote_average: Float
)

/**
 * Converts [List<DatabaseFavourite>] to List<MovieOverview> for dealing with it in the app.
 * @return converted list
 */
@JvmName("asDomainModelDatabaseFavourite")
fun List<DatabaseFavourite>.asDomainModel(): List<MovieOverview> {
    return map {
        MovieOverview(
            id = it.id,
            title = it.title,
            poster_path = it.poster_path,
            release_date = it.release_date,
            vote_average = it.vote_average
        )
    }
}

/**
 * Data class for storing movie overview by [category].
 *
 * @property id movie id from TMDB
 * @property title English movie title
 * @property poster_path poster path, nullable
 * @property release_date date string
 * @property vote_average average movie score
 * @property category movie category (i.e. Latest, Top Rated, etc.)
 */
@Entity
data class DatabaseMovieOverview constructor(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster_path: String?,
    val release_date: String,
    val vote_average: Float,
    val category: String
)

/**
 * Converts [List<DatabaseMovieOverview>] to List<MovieOverview> for dealing with it in the app.
 * @return converted list
 */
fun List<DatabaseMovieOverview>.asDomainModel(): List<MovieOverview> {
    return map {
        MovieOverview(
            id = it.id,
            title = it.title,
            poster_path = it.poster_path,
            release_date = it.release_date,
            vote_average = it.vote_average
        )
    }
}
