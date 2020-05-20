package com.federicopintaluba.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.federicopintaluba.popularmovies.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MovieAdapter.MovieItemClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ContentLoadingProgressBar progressBar;
    private String[] sortingOptions = {"Popular", "Top rated"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        Spinner spinner = findViewById(R.id.sort_by_spinner);
        ArrayAdapter sortingOptionsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortingOptions);
        sortingOptionsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortingOptionsArrayAdapter);
        spinner.setOnItemSelectedListener(this);

        progressBar = findViewById(R.id.progress_bar);

        makeNetworkCall(sortingOptions[0]);
    }

    private void setUpAdapter(List<Movie> movies) {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(this, movies, this);
            recyclerView.setAdapter(movieAdapter);
        } else {
            movieAdapter.updateDataSource(movies);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        makeNetworkCall(sortingOptions[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void makeNetworkCall(String sortingOption) {
        new NetworkCall().execute(NetworkUtils.buildUrl(sortingOption.endsWith("Popular") ? NetworkEndpoint.MOVIE_POPULAR : NetworkEndpoint.MOVIE_TOP_RATED));
    }

    @Override
    public void onMovieItemClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(IntentKeys.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    public class NetworkCall extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.show();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];

            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                List<Movie> movies = JsonUtils.parseMovieListJson(s);
                setUpAdapter(movies);
            }

            progressBar.hide();
        }
    }
}
