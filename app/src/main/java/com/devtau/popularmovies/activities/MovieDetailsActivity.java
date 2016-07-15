package com.devtau.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.devtau.popularmovies.R;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Constants;
import com.devtau.popularmovies.util.Util;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String MOVIE_EXTRA = "movieExtra";
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movie = getIntent().getParcelableExtra(MOVIE_EXTRA);
        initControls();
    }

    private void initControls() {
        if(movie == null) return;//method needs valid movie

        //find controls
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        ImageView iv_poster = (ImageView) findViewById(R.id.iv_poster);
        TextView tv_release_date = (TextView) findViewById(R.id.tv_release_date);
        TextView tv_user_rating = (TextView) findViewById(R.id.tv_user_rating);
        TextView tv_plot_synopsis = (TextView) findViewById(R.id.tv_plot_synopsis);

        //initiate links with data
        tv_title.setText(movie.getTitle());
        Util.loadImageToView(this, movie.getPosterPath(), iv_poster,
                Constants.DEFAULT_POSTER_WIDTH, Constants.DEFAULT_POSTER_HEIGHT);
        tv_release_date.setText(getReleaseYear());
        tv_user_rating.setText(getUserRating());
        tv_plot_synopsis.setText(movie.getPlotSynopsis());
    }

    private String getReleaseYear() {
        Calendar defaultDate = new GregorianCalendar(1970, 0, 1);
        if(movie.getReleaseDate().compareTo(defaultDate) == 0) {
            return "---";
        } else {
            return String.valueOf(movie.getReleaseDate().get(Calendar.YEAR));
        }
    }

    private String getUserRating() {
        Locale locale = getResources().getConfiguration().locale;
        String userRating = String.valueOf(movie.getUserRating());
        return String.format(locale, getString(R.string.user_rating_formatter), userRating);
    }
}
