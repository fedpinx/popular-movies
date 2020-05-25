package com.federicopintaluba.popularmovies;

class NetworkEndpoint {

    final static String MOVIE_POPULAR = "/movie/popular";
    final static String MOVIE_TOP_RATED = "/movie/top_rated";
    final static String MOVIE_VIDEOS = "/movie/{id}/videos";
    final static String MOVIE_REVIEWS = "/movie/{id}/reviews";

    private NetworkEndpoint() {
    }
}