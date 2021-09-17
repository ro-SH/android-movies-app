package com.example.moviesapp.data.source.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesapp.data.MovieOverview

@Entity
data class DatabaseFavourite constructor(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster_path: String?,
    val release_date: String,
    val vote_average: Float
)

fun DatabaseFavourite.getId(): Int = id

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
