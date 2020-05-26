package com.federicopintaluba.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.federicopintaluba.popularmovies.model.Movie;

import java.util.List;

class FavoriteMoviesViewModel extends ViewModel {

    private FavoriteMovieDatabase database;

    private LiveData<List<Movie>> favoriteMovies;

    FavoriteMoviesViewModel(FavoriteMovieDatabase database) {
        this.database = database;
        this.favoriteMovies = database.favoriteMovieDao().getAllFavoriteMovies();
    }

    LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    LiveData<Movie> getFavoriteMovie(Integer movieId) {
        return this.database.favoriteMovieDao().getFavoriteMovie(movieId);
    }

    void insertFavoriteMovie(Movie movie) {
        this.database.favoriteMovieDao().insertFavoriteMovie(movie);
    }

    void deleteFavoriteMovie(Movie movie) {
        this.database.favoriteMovieDao().deleteFavoriteMovie(movie);
    }
}
