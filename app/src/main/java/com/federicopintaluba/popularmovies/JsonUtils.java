package com.federicopintaluba.popularmovies;

import com.federicopintaluba.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    // Movie JSON Object attributes
    private final static String MOVIE_DISCOVER_RESULTS = "results";
    private final static String MOVIE_POPULARITY = "popularity";
    private final static String MOVIE_VOTE_COUNT = "vote_count";
    private final static String MOVIE_VIDEO = "video";
    private final static String MOVIE_POSTER_PATH = "poster_path";
    private final static String MOVIE_ID = "id";
    private final static String MOVIE_ADULT = "adult";
    private final static String MOVIE_BACKDROP_PATH = "backdrop_path";
    private final static String MOVIE_ORIGINAL_LANGUAGE = "original_language";
    private final static String MOVIE_ORIGINAL_TITLE = "original_title";
    private final static String MOVIE_GENRE_IDS = "genre_ids";
    private final static String MOVIE_TITLE = "title";
    private final static String MOVIE_VOTE_AVERAGE = "vote_average";
    private final static String MOVIE_OVERVIEW = "overview";
    private final static String MOVIE_RELEASE_DATE = "release_date";


    public static List<Movie> parseMovieListJson(String json) {
        try {
            List<Movie> listOfMovies = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(json);
            JSONArray resultsArray = jsonObject.getJSONArray(MOVIE_DISCOVER_RESULTS);
            for (int i = 0; i < resultsArray.length(); i++) {
                final Movie movie = new Movie();

                JSONObject movieObject = resultsArray.getJSONObject(i);
                movie.setPopularity(movieObject.getLong(MOVIE_POPULARITY));
                movie.setVoteCount(movieObject.getInt(MOVIE_VOTE_COUNT));
                movie.setVideo(movieObject.getBoolean(MOVIE_VIDEO));
                movie.setPosterPath(movieObject.getString(MOVIE_POSTER_PATH));
                movie.setId(movieObject.getInt(MOVIE_ID));
                movie.setAdult(movieObject.getBoolean(MOVIE_ADULT));
                movie.setBackdropPath(movieObject.getString(MOVIE_BACKDROP_PATH));
                movie.setOriginalLanguage(movieObject.getString(MOVIE_ORIGINAL_LANGUAGE));
                movie.setOriginalTitle(movieObject.getString(MOVIE_ORIGINAL_TITLE));
                JSONArray genreIdsArray = movieObject.getJSONArray(MOVIE_GENRE_IDS);
                Integer[] genreIds = new Integer[genreIdsArray.length()];
                for (int j = 0; j < genreIdsArray.length(); j++) {
                    genreIds[j] = genreIdsArray.getInt(j);
                }
                movie.setGenreIds(genreIds);
                movie.setTitle(movieObject.getString(MOVIE_TITLE));
                movie.setVoteAverage(movieObject.getInt(MOVIE_VOTE_AVERAGE));
                movie.setOverview(movieObject.getString(MOVIE_OVERVIEW));
                movie.setReleaseDate(movieObject.getString(MOVIE_RELEASE_DATE));

                listOfMovies.add(movie);
            }

            return listOfMovies;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
