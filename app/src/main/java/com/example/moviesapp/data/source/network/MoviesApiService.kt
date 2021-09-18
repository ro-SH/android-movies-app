package com.example.moviesapp.data.source.network

import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieObject
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY = "6de66c8f8c90c84ca28af4d94b94794b"
private const val BASE_URL = "https://api.themoviedb.org/3/"
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

private val gson = GsonBuilder()
    .create()

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

interface MoviesApiService {
    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") id: Int, @Query("api_key") api_key: String = API_KEY): Movie

    @GET("movie/upcoming")
    suspend fun getLatest(@Query("api_key") api_key: String = API_KEY): MovieObject

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") api_key: String = API_KEY): MovieObject

    @GET("movie/popular")
    suspend fun getPopular(@Query("api_key") api_key: String = API_KEY): MovieObject

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("api_key") api_key: String = API_KEY): MovieObject

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") search: String,
        @Query("api_key") api_key: String = API_KEY
    ): MovieObject
}

object MoviesApi {
    val retrofitService: MoviesApiService by lazy { retrofit.create(MoviesApiService::class.java) }
}
