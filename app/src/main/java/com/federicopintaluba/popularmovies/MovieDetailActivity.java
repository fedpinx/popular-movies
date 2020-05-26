package com.federicopintaluba.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.federicopintaluba.popularmovies.model.Movie;
import com.federicopintaluba.popularmovies.model.MovieReview;
import com.federicopintaluba.popularmovies.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements MovieTrailersAdapter.MovieTrailerItemClickListener {

    private ContentLoadingProgressBar progressBar;
    private MovieTrailersAdapter movieTrailersAdapter;
    private MovieReviewsAdapter movieReviewsAdapter;
    private RecyclerView movieTrailersRecyclerView;
    private RecyclerView movieReviewsRecyclerView;
    private FavoriteMoviesViewModel viewModel;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        progressBar = findViewById(R.id.progress_bar);

        movieTrailersRecyclerView = findViewById(R.id.movie_trailers_recycler_view);
        movieTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieTrailersRecyclerView.setHasFixedSize(true);

        movieReviewsRecyclerView = findViewById(R.id.movie_reviews_recycler_view);
        movieReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieReviewsRecyclerView.setHasFixedSize(true);

        viewModel = new ViewModelProvider(this,
                new FavoriteMoviesViewModelFactory(FavoriteMovieDatabase.getInstance(this)))
                .get(FavoriteMoviesViewModel.class);

        Movie movie = getIntent().getParcelableExtra(IntentKeys.EXTRA_MOVIE);
        populateUI(movie);

        makeVideosNetworkCall(movie.getId());
        makeReviewsNetworkCall(movie.getId());
    }

    private void setUpTrailersAdapter(List<MovieTrailer> movieTrailers) {
        if (movieTrailersAdapter == null) {
            movieTrailersAdapter = new MovieTrailersAdapter(this, movieTrailers, this);
            movieTrailersRecyclerView.setAdapter(movieTrailersAdapter);
        } else {
            movieTrailersAdapter.updateDataSource(movieTrailers);
        }
    }

    private void setUpReviewsAdapter(List<MovieReview> movieReviews) {
        if (movieReviewsAdapter == null) {
            movieReviewsAdapter = new MovieReviewsAdapter(this, movieReviews);
            movieReviewsRecyclerView.setAdapter(movieReviewsAdapter);
        } else {
            movieReviewsAdapter.updateDataSource(movieReviews);
        }
    }

    private void populateUI(final Movie movie) {
        if (movie != null) {
            final TextView movieTitle = findViewById(R.id.movie_title);
            final TextView movieReleaseDate = findViewById(R.id.movie_release_date);
            final TextView movieVoteAverage = findViewById(R.id.movie_vote_average);
            final TextView movieOverview = findViewById(R.id.movie_overview);
            final ImageView moviePoster = findViewById(R.id.movie_poster);
            final ImageButton favoriteButton = findViewById(R.id.movie_favorite);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavorite) {
                        isFavorite = false;
                        favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_48dp);

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                viewModel.deleteFavoriteMovie(movie);
                            }
                        });
                    } else {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                viewModel.insertFavoriteMovie(movie);
                            }
                        });

                        isFavorite = true;
                        favoriteButton.setImageResource(R.drawable.ic_favorite_white_48dp);
                    }
                }
            });

            viewModel.getFavoriteMovie(movie.getId()).observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(Movie favoriteMovie) {
                    if (favoriteMovie != null) {
                        isFavorite = true;
                        favoriteButton.setImageResource(R.drawable.ic_favorite_white_48dp);
                    } else {
                        isFavorite = false;
                        favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                    }
                }
            });

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

    private void makeVideosNetworkCall(Integer movieId) {
        new MovieDetailActivity.ReviewsNetworkCall().execute(NetworkUtils.buildUrl(NetworkEndpoint.MOVIE_REVIEWS, movieId));
    }

    private void makeReviewsNetworkCall(Integer movieId) {
        new MovieDetailActivity.TrailersNetworkCall().execute(NetworkUtils.buildUrl(NetworkEndpoint.MOVIE_VIDEOS, movieId));
    }

    @Override
    public void onMovieTrailerItemClick(MovieTrailer movieTrailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(NetworkUtils.buildMovieTrailerPath(movieTrailer.getKey())));
        startActivity(intent);
    }

    public class TrailersNetworkCall extends AsyncTask<URL, Void, String> {

        boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            progressBar.show();
        }

        @Override
        protected String doInBackground(URL... urls) {
            if (NetworkUtils.isOnline()) {
                URL url = urls[0];

                String result = null;
                try {
                    result = NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result;
            } else {
                noInternet = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                List<MovieTrailer> movieTrailers = JsonUtils.parseMovieTrailersListJson(s);
                setUpTrailersAdapter(movieTrailers);
            }

            if (noInternet) {
                Toast.makeText(getApplicationContext(), R.string.no_connection_error, Toast.LENGTH_LONG).show();
                progressBar.hide();
            }

            progressBar.hide();
        }
    }

    public class ReviewsNetworkCall extends AsyncTask<URL, Void, String> {

        boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            progressBar.show();
        }

        @Override
        protected String doInBackground(URL... urls) {
            if (NetworkUtils.isOnline()) {
                URL url = urls[0];

                String result = null;
                try {
                    result = NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result;
            } else {
                noInternet = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                List<MovieReview> movieReviews = JsonUtils.parseMovieReviewsListJson(s);
                setUpReviewsAdapter(movieReviews);
            }

            if (noInternet) {
                Toast.makeText(getApplicationContext(), R.string.no_connection_error, Toast.LENGTH_LONG).show();
                progressBar.hide();
            }

            progressBar.hide();
        }
    }
}
