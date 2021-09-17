package com.example.moviesapp.data.source.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.TypeConverters
import com.example.moviesapp.data.MovieGenre

@Dao
interface MoviesDao {
    @Query("select * from databasefavourite where id = :movieId")
    fun getMovie(movieId: Int): DatabaseFavourite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(favourite: DatabaseFavourite)

    // Delete Movie from Favourites by id
    @Query("delete from databasefavourite where id = :movieId")
    fun deleteMovie(movieId: Int)

    // Get Favourites
    @Query("select * from databasefavourite")
    fun getFavourites(): LiveData<List<DatabaseFavourite>>

    // Insert List of Movie Overviews
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieOverviews(vararg movies: DatabaseMovieOverview)

    // Get List of Movie Overviews by category
    @Query("select * from databasemovieoverview where category = :category")
    fun getMovieOverviews(category: String): LiveData<List<DatabaseMovieOverview>>

    // Clear Movie Overviews
    @Query("delete from databasemovieoverview where category = :category")
    fun clearMovieOverviews(category: String)
}

// Database class
@Database(entities = [DatabaseFavourite::class, DatabaseMovieOverview::class], version = 2)
@TypeConverters(Converters::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
}

// Database instance
private lateinit var DATABASE_INSTANCE: MoviesDatabase

// Get Database instance
fun getDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::DATABASE_INSTANCE.isInitialized) {
            DATABASE_INSTANCE = Room.databaseBuilder(
                context,
                MoviesDatabase::class.java,
                "Movies"
            ).fallbackToDestructiveMigration().build()
        }
        return DATABASE_INSTANCE
    }
}

class Converters {
    @TypeConverter
    fun fromGenres(genres: Array<MovieGenre>): String {
        return genres.joinToString(", ")
    }

    @TypeConverter
    fun toGenres(data: String): Array<MovieGenre> {
        val result = mutableListOf<MovieGenre>()
        for (item in data.split(", ")) {
            result.add(MovieGenre(id = item.split(": ")[0].toInt(), name = item.split(": ")[1]))
        }

        return result.toTypedArray()
    }
}
