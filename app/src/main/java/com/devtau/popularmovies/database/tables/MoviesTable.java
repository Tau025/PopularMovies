package com.devtau.popularmovies.database.tables;

import android.content.ContentValues;
import android.provider.BaseColumns;
import com.devtau.popularmovies.database.MySQLHelper;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Util;

public abstract class MoviesTable {
    public static final String TABLE_NAME = "Movie";

    public static final String TITLE = "title";
    public static final String POSTER_PATH = "posterPath";
    public static final String PLOT_SYNOPSIS = "plotSynopsis";
    public static final String USER_RATING = "userRating";
    public static final String RELEASE_DATE = "releaseDate";

    public static final String FIELDS = MySQLHelper.PRIMARY_KEY
            + TITLE + " TEXT, "
            + POSTER_PATH + " TEXT, "
            + PLOT_SYNOPSIS + " TEXT, "
            + USER_RATING + " REAL, "
            + RELEASE_DATE + " TEXT";

    public static ContentValues getContentValues(Movie item) {
        ContentValues cv = new ContentValues();
        if (item.getId() != -1) {
            cv.put(BaseColumns._ID, item.getId());
        }
        cv.put(TITLE, item.getTitle());
        cv.put(POSTER_PATH, item.getPosterPath());
        cv.put(PLOT_SYNOPSIS, item.getPlotSynopsis());
        cv.put(USER_RATING, item.getUserRating());
        cv.put(RELEASE_DATE, Util.dateFormat.format(item.getReleaseDate().getTime()));
        return cv;
    }
}