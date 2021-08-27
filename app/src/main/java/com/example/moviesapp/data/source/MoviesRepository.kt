package com.example.moviesapp.data.source

import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieOverview
import com.example.moviesapp.data.source.network.MoviesApi

class MoviesRepository {
    suspend fun getMovie(id: String): Movie = MoviesApi.retrofitService.getMovie(id)

    suspend fun getLatest(): List<MovieOverview> = MoviesApi.retrofitService.getLatest().results

    suspend fun getNowPlaying(): List<MovieOverview> = MoviesApi.retrofitService.getNowPlaying().results

    suspend fun getPopular(): List<MovieOverview> = MoviesApi.retrofitService.getPopular().results

    suspend fun getTopRated(): List<MovieOverview> = MoviesApi.retrofitService.getTopRated().results
}
