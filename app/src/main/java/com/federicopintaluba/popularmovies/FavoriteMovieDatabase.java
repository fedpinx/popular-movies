package com.federicopintaluba.popularmovies;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.federicopintaluba.popularmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoriteMovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "FavoriteMovies";
    private static FavoriteMovieDatabase sInstance;

    static FavoriteMovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteMovieDatabase.class, DATABASE_NAME).build();
            }
        }

        return sInstance;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();
}
