package com.devtau.popularmovies.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.devtau.popularmovies.R;
import com.devtau.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.devtau.popularmovies.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String MOVIE_EXTRA = "movieExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Movie movie = getIntent().getParcelableExtra(MOVIE_EXTRA);
        initControls(movie);
    }

    private void initControls(Movie movie) {
        if(movie == null) return;//method needs valid movie

        //https://developer.android.com/topic/libraries/data-binding/index.html
        //https://stfalcon.com/en/blog/post/faster-android-apps-with-databinding
        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        binding.setMovie(movie);
    }
}
