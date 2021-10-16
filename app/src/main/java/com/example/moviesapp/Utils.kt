package com.example.moviesapp

import android.graphics.Color
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieOverview
import com.example.moviesapp.data.source.network.IMAGE_BASE_URL

fun getGenresFromMovie(movie: Movie): String =
    "• " + movie.genres.joinToString(separator = " • ") { it.name }

fun getYearFromMovie(movie: Movie): String = movie.release_date.substringBefore('-')

fun getYearFromMovie(movie: MovieOverview): String = if (movie.release_date.isNullOrEmpty()) "No Data" else movie.release_date.substringBefore('-')

fun getOriginalTitleFromMovie(movie: Movie): String =
    "${movie.original_title}, ${getYearFromMovie(movie)}"

fun getRunTimeFromMovie(movie: Movie): String = "${movie.runtime} minutes"

fun getScoreBackground(score: Float) = when {
    score >= 7 -> Color.parseColor("#22bb33")
    score >= 4 -> Color.parseColor("#f0ad4e")
    else -> Color.parseColor("#bb2124")
}

fun getTaglineFromMovie(movie: Movie): String = "\"${movie.tagline}\""

fun getVotesFromMovie(movie: Movie): String = "${movie.vote_count} votes"

fun ImageView.loadOverviewPoster(url: String?) {
    if (url.isNullOrEmpty()) {
        this.setImageDrawable(AppCompatResources.getDrawable(this.context, R.drawable.noimage))
    } else {
        val imgUrl = IMAGE_BASE_URL + url
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        val circleProgressDrawable = CircularProgressDrawable(this.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        Glide.with(this.context)
            .load(imgUri)
            .override(500, 500)
            .placeholder(circleProgressDrawable)
            .fitCenter()
            .into(this)
    }
}
