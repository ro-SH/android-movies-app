package com.example.moviesapp.data.source.network

import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieObject
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Api key for accessing TMDB Api
private const val API_KEY = "6de66c8f8c90c84ca28af4d94b94794b"

// Base url for accessing TMDB Api
private const val BASE_URL = "https://api.themoviedb.org/3/"

// Base url for accessing images in TMDB Api
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

// Gson object
private val gson = GsonBuilder()
    .create()

// Retrofit instance with GsonConverterFactory
private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

/**
 * Api service for getting info from TMDB.
 */
interface MoviesApiService {

    /**
     * Get full movie information from the API by [id].
     * @param id movie id
     * @param api_key
     * @return Movie instance
     */
    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") id: Int, @Query("api_key") api_key: String = API_KEY): Movie

    /**
     * Get list of Upcoming movies.
     * @param api_key
     * @return MovieObject
     */
    @GET("movie/upcoming")
    suspend fun getLatest(@Query("api_key") api_key: String = API_KEY): MovieObject

    /**
     * Get list of Now Playing movies.
     * @param api_key
     * @return MovieObject
     */
    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") api_key: String = API_KEY): MovieObject

    /**
     * Get list of Popular movies.
     * @param api_key
     * @return MovieObject
     */
    @GET("movie/popular")
    suspend fun getPopular(@Query("api_key") api_key: String = API_KEY): MovieObject

    /**
     * Get list of Top Rated movies.
     * @param api_key
     * @return MovieObject
     */
    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("api_key") api_key: String = API_KEY): MovieObject

    /**
     * Get list of found movies by [search].
     * @param search query to search movies.
     * @param api_key
     * @return MovieObject
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") search: String,
        @Query("api_key") api_key: String = API_KEY
    ): MovieObject
}

/**
 * Movie Api object.
 */
object MoviesApi {
    // retrofit service
    val retrofitService: MoviesApiService by lazy { retrofit.create(MoviesApiService::class.java) }
}
