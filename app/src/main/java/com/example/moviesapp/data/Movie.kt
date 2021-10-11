package com.example.moviesapp.data

import com.example.moviesapp.data.source.database.DatabaseFavourite
import com.google.gson.annotations.SerializedName

/**
 * Movie genre data class for accessing movie genres.
 * @property id genre id from TMDB
 * @property name genre name from TMDB
 */
data class MovieGenre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    override fun toString(): String = "$id: $name"
}

/**
 * Movie data class for interacting with movie info from TMDb in the app.
 * @property id movie id from TMDB
 * @property title English movie title
 * @property original_title original movie title
 * @property overview text of movie overview
 * @property genres array of MovieGenre
 * @property poster_path poster path or null
 * @property release_date
 * @property runtime movie runtime or null
 * @property tagline movie tagline or null
 * @property vote_average average movie score
 * @property vote_count number of votes
 */
data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val original_title: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("genres") val genres: Array<MovieGenre>,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("vote_average") val vote_average: Float,
    @SerializedName("vote_count") val vote_count: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false
        if (title != other.title) return false
        if (original_title != other.original_title) return false
        if (overview != other.overview) return false
        if (!genres.contentEquals(other.genres)) return false
        if (poster_path != other.poster_path) return false
        if (release_date != other.release_date) return false
        if (runtime != other.runtime) return false
        if (tagline != other.tagline) return false
        if (vote_average != other.vote_average) return false
        if (vote_count != other.vote_count) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + original_title.hashCode()
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + genres.contentHashCode()
        result = 31 * result + (poster_path?.hashCode() ?: 0)
        result = 31 * result + release_date.hashCode()
        result = 31 * result + (runtime ?: 0)
        result = 31 * result + (tagline?.hashCode() ?: 0)
        result = 31 * result + vote_average.hashCode()
        result = 31 * result + vote_count
        return result
    }
}

/**
 * Convert Movie to DatabaseFavourite for saving in database.
 * @return movie converted to DataBaseFavourite
 */
fun Movie.asDatabaseModel(): DatabaseFavourite =
    DatabaseFavourite(id, title, poster_path, release_date, vote_average)
