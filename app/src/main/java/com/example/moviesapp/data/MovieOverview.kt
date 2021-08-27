package com.example.moviesapp.data

import com.google.gson.annotations.SerializedName

data class MovieObject(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: MutableList<MovieOverview>
)

data class MovieOverview(
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val original_title: String,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("vote_average") val vote_average: Float
)
