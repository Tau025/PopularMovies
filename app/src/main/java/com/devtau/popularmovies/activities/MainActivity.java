package com.devtau.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.devtau.popularmovies.fragments.MovieFragment;
import com.devtau.popularmovies.fragments.ProgressBarDF;
import com.devtau.popularmovies.R;
import com.devtau.popularmovies.database.DataSource;
import com.devtau.popularmovies.database.sources.MoviesSource;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Constants;
import com.devtau.popularmovies.util.Logger;

public class MainActivity extends AppCompatActivity implements
        MovieFragment.OnListFragmentInteractionListener {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.isDebug = true;
        setContentView(R.layout.activity_main);

        if(selfCheck()) {
            sendRequest();
        }
    }

    private boolean selfCheck() {
        String errorMsg = "";
        if("".equals(Constants.API_KEY_VALUE)) {
            errorMsg = getString(R.string.no_API_key);
        } else if(Constants.API_KEY_VALUE.length() != 32) {
            errorMsg = getString(R.string.API_key_length_is_bad);
        }
        if(!checkIsOnline()) {
            if(!"".equals(errorMsg)) {
                errorMsg += getString(R.string.also);
            }
            errorMsg += getString(R.string.no_internet);
        }
        if(!"".equals(errorMsg)) {
            addErrorTextViewToLayout(errorMsg);
        }
        return "".equals(errorMsg);
    }

    private boolean checkIsOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void addErrorTextViewToLayout(String errorMsg) {
        TextView errorTextView = new TextView(MainActivity.this);
        errorTextView.setText(errorMsg);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        errorTextView.setLayoutParams(layoutParams);
        ((LinearLayout) findViewById(R.id.layoutMain)).addView(errorTextView);
    }

    private void sendRequest() {
        new ProgressBarDF().show(getSupportFragmentManager(), ProgressBarDF.TAG);
        MoviesSource moviesSource = new DataSource(this).getMoviesSource();
        //First we'll get cached list from local db. Then we'll send request to server and update if necessary
        MovieFragment movieFragment =  MovieFragment.newInstance(2, moviesSource.getItemsList());
        addFragmentToLayout(R.id.moviesListPlaceholder, movieFragment);
    }

    private void addFragmentToLayout(int placeholderID, Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(placeholderID, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dismissProgressBar(){
        //delay applied in order to avoid flickering, when connection is fast
        new Handler().postDelayed(() -> {
            ProgressBarDF dialog = (ProgressBarDF) getSupportFragmentManager().findFragmentByTag(ProgressBarDF.TAG);
            if (dialog != null) {
                dialog.dismiss();
            } else {
                Logger.d(LOG_TAG, "dialog already dismissed");
            }
        }, 700);
    }

    @Override
    public void onListFragmentInteraction(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_EXTRA, movie);
        startActivity(intent);
    }
}
