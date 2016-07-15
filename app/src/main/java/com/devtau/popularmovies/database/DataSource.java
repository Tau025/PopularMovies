package com.devtau.popularmovies.database;

import android.content.Context;
import com.devtau.popularmovies.database.sources.MoviesSource;
import com.devtau.popularmovies.model.Movie;
import java.util.GregorianCalendar;

public class DataSource {
    private MoviesSource itemsSource;

    public DataSource(Context context) {
        itemsSource = new MoviesSource(context);

//        if (itemsSource.getItemsCount() == 0) {
//            populateDB();
//        }
    }

    public MoviesSource getMoviesSource() {
        return itemsSource;
    }

    private void populateDB() {
        itemsSource.create(new Movie(new GregorianCalendar(2016, 5, 26, 14, 0), 2000, "Movie one", ""));//month +1
        itemsSource.create(new Movie(new GregorianCalendar(2016, 5, 27, 10, 10), 500, "Movie two", ""));
        itemsSource.create(new Movie(new GregorianCalendar(2016, 5, 28, 8, 40), 1000, "Movie three", ""));
        itemsSource.create(new Movie(new GregorianCalendar(2016, 6, 1, 10, 0), 800, "Movie four", ""));
        itemsSource.create(new Movie(new GregorianCalendar(2016, 6, 3, 12, 30), 150, "Movie five", ""));
        itemsSource.create(new Movie(new GregorianCalendar(2016, 6, 5, 9, 20), 400, "Movie six", ""));
    }
}
