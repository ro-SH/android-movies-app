package com.example.moviesapp

import com.example.moviesapp.data.Movie

fun getAverageFromMovie(movie: Movie): String = "Average: ${movie.vote_average} Count: ${movie.vote_count}"

fun getInfoFromMovie(movie: Movie): String {
    var result = "${movie.release_date.substringBefore('-')}, "
    for ((index, genre) in movie.genres.withIndex()) {
        result += genre.name
        if (index != movie.genres.size - 1) {
            result += ", "
        }
    }

    return result
}

fun getOriginalTitleFromMovie(movie: Movie): String = "Original title: ${movie.original_title}"

fun getRunTimeFromMovie(movie: Movie): String = "${movie.runtime} minutes"
