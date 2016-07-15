package com.devtau.popularmovies.database;

import android.content.Context;
import com.devtau.popularmovies.database.sources.MoviesSource;

public class DataSource {
    private MoviesSource itemsSource;

    public DataSource(Context context) {
        itemsSource = new MoviesSource(context);
    }

    public MoviesSource getMoviesSource() {
        return itemsSource;
    }
}
