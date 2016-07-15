package com.devtau.popularmovies.util;

public abstract class Constants {
    public static final String IMAGE_STORAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    //    posterSize: "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    public static final String POSTER_SIZE = "w342";

    // These are the names of the JSON objects that need to be extracted.
    public static final String RESULTS = "results";
    public static final String ID = "id";
    public static final String TITLE = "original_title";
    public static final String POSTER_PATH = "poster_path";
    public static final String PLOT_SYNOPSIS = "overview";
    public static final String USER_RATING = "vote_average";
    public static final String RELEASE_DATE = "release_date";

    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    public static final String SORT_BY_PARAM = "sort_by";
    public static final String API_KEY_PARAM = "api_key";
    public static final String API_KEY_VALUE = "";
}
