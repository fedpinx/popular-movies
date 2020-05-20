package com.federicopintaluba.popularmovies;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.federicopintaluba.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie movie = getIntent().getParcelableExtra(IntentKeys.EXTRA_MOVIE);
        populateUI(movie);
    }

    private void populateUI(Movie movie) {
        if (movie != null) {
            TextView movieTitle = findViewById(R.id.movie_title);
            TextView movieReleaseDate = findViewById(R.id.movie_release_date);
            TextView movieVoteAverage = findViewById(R.id.movie_vote_average);
            TextView movieOverview = findViewById(R.id.movie_overview);
            ImageView moviePoster = findViewById(R.id.movie_poster);

            movieTitle.setText(movie.getTitle());
            movieReleaseDate.setText(movie.getReleaseDate());
            movieVoteAverage.setText(String.format(getString(R.string.vote_rating_format),
                    movie.getVoteAverage()));
            movieOverview.setText(movie.getOverview());

            Picasso.with(this)
                    .load(NetworkUtils.buildMoviePosterPath(movie.getPosterPath()))
                    .into(moviePoster);
        } else {
            throw new NullPointerException("You must provide a Movie object as EXTRA_MOVIE when starting MovieDetailActivity.");
        }
    }
}
