package com.devtau.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.devtau.popularmovies.R;
import com.devtau.popularmovies.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String MOVIE_EXTRA = "movieExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Movie movie = getIntent().getParcelableExtra(MOVIE_EXTRA);
        String msg = movie.toString();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
