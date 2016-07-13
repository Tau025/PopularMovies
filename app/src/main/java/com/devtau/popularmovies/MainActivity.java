package com.devtau.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.devtau.popularmovies.database.DataSource;
import com.devtau.popularmovies.database.sources.MoviesSource;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Logger;

public class MainActivity extends AppCompatActivity implements
        MovieFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkIsOnline()){
            sendRequest();
        } else {
            String msg = getString(R.string.no_internet);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkIsOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void sendRequest() {
        new ProgressBarDF().show(getSupportFragmentManager(), ProgressBarDF.TAG);
        MoviesSource moviesSource = new DataSource(this).getDummyItemsSource();
        MovieFragment movieFragment =  MovieFragment.newInstance(2, moviesSource.getItemsList());
        addFragmentToLayout(R.id.movieListPlaceholder, movieFragment);
        //TODO: put dismissProgressBar() to callbacks
        new Handler().postDelayed(this::dismissProgressBar, 1000);
    }

    private void addFragmentToLayout(int placeholderID, Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(placeholderID, fragment);
        ft.commit();
    }

    private boolean dismissProgressBar(){
        ProgressBarDF dialog = (ProgressBarDF) getSupportFragmentManager().findFragmentByTag(ProgressBarDF.TAG);
        if (dialog != null) {
            dialog.dismiss();
            return true;
        } else {
            Logger.d(getString(R.string.dialog_already_dismissed));
            return false;
        }
    }


    @Override
    public void onListFragmentInteraction(Movie movie) {
        String msg = movie.toString();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
