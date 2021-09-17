package com.example.moviesapp.data

import com.example.moviesapp.data.source.database.DatabaseMovieOverview
import com.google.gson.annotations.SerializedName

const val LATEST = "latest"
const val POPULAR = "popular"
const val NOW_PLAYING = "now playing"
const val TOP_RATED = "top rated"

data class MovieObject(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<MovieOverview>
)

data class MovieOverview(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("vote_average") val vote_average: Float
)

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
