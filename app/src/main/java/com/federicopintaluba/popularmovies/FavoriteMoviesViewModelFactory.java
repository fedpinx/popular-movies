package com.federicopintaluba.popularmovies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FavoriteMoviesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final FavoriteMovieDatabase database;

    public FavoriteMoviesViewModelFactory(FavoriteMovieDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoriteMoviesViewModel(database);
    }
}
