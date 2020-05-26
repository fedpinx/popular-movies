package com.federicopintaluba.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.federicopintaluba.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getAllFavoriteMovies();

    @Query("SELECT * from Movie WHERE id = :id")
    LiveData<Movie> getFavoriteMovie(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);
}
