package com.example.moviesapp.data.source.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesapp.data.MovieGenre

/**
 * Interface for working with Database.
 */
@Dao
interface MoviesDao {
    /**
     * Get DatabaseFavourite from database by [movieId].
     * @param movieId
     * @return movie info from Database by [movieId]
     */
    @Query("select * from databasefavourite where id = :movieId")
    fun getMovie(movieId: Int): DatabaseFavourite?

    /**
     * Insert one movie to DatabaseFavourite.
     * @param favourite movie info
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(favourite: DatabaseFavourite)

    /**
     * Delete movie from DatabaseFavourite by [movieId].
     * @param movieId
     */
    @Query("delete from databasefavourite where id = :movieId")
    fun deleteMovie(movieId: Int)

    /**
     * Get all the movies from DatabaseFavourite.
     * @return LiveData of movies
     */
    @Query("select * from databasefavourite")
    fun getFavourites(): LiveData<List<DatabaseFavourite>>

    /**
     * Insert movies to DatabaseMovieOverview.
     * @param movies movies info
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieOverviews(vararg movies: DatabaseMovieOverview)

    /**
     * Get all the movies overviews of certain [category].
     * @param category (i.e. Latest, Top rated, etc.)
     * @return LiveData of movies
     */
    @Query("select * from databasemovieoverview where category = :category")
    fun getMovieOverviews(category: String): LiveData<List<DatabaseMovieOverview>>

    /**
     * Delete all the movies of certain [category] from DatabaseMovieOverview.
     * @param category
     */
    // Clear Movie Overviews
    @Query("delete from databasemovieoverview where category = :category")
    fun clearMovieOverviews(category: String)
}

/**
 * Room Database class.
 */
@Database(entities = [DatabaseFavourite::class, DatabaseMovieOverview::class], version = 3)
@TypeConverters(Converters::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
}

// Database instance
private lateinit var DATABASE_INSTANCE: MoviesDatabase

/**
 * Create [DATABASE_INSTANCE] if not initialized and return it.
 * @param context application context
 * @return [DATABASE_INSTANCE]
 */
fun getDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::DATABASE_INSTANCE.isInitialized) {
            DATABASE_INSTANCE = Room.databaseBuilder(
                context,
                MoviesDatabase::class.java,
                "Movies"
            ).build()
        }
        return DATABASE_INSTANCE
    }
}

/**
 * Type converted for movie genre.
 */
class Converters {
    /**
     * Converts Array of MovieGenre to a string by separating values with " ,".
     * @param genres array of MovieGenre
     * @return converted array of MovieGenre to a string.
     */
    @TypeConverter
    fun fromGenres(genres: Array<MovieGenre>): String {
        return genres.joinToString(", ")
    }

    /**
     * Converts string to an array of MovieGenre by splitting string.
     * @param data string of movie genres
     * @return array of MovieGenre
     */
    @TypeConverter
    fun toGenres(data: String): Array<MovieGenre> {
        val result = mutableListOf<MovieGenre>()
        for (item in data.split(", ")) {
            result.add(MovieGenre(id = item.split(": ")[0].toInt(), name = item.split(": ")[1]))
        }

        return result.toTypedArray()
    }
}
