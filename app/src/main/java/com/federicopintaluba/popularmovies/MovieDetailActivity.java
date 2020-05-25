package com.federicopintaluba.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
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
