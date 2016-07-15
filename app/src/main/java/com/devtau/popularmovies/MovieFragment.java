package com.devtau.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devtau.popularmovies.database.DataSource;
import com.devtau.popularmovies.database.sources.MoviesSource;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Constants;
import com.devtau.popularmovies.util.Logger;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment {
    private static final String ARG_ITEMS_LIST = "itemsList";
    private static final String ARG_COLUMN_COUNT = "columnCount";
    private List<Movie> moviesList;
    private int columnCount = 2;
    private OnListFragmentInteractionListener listener;
    private int imageWidth, imageHeight;
    private MyMovieRecyclerViewAdapter rvAdapter;

    public MovieFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnListFragmentInteractionListener) getParentFragment();
            if (listener == null) {
                listener = (OnListFragmentInteractionListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    public static MovieFragment newInstance(int columnCount, ArrayList<Movie> moviesList) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ITEMS_LIST, moviesList);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            moviesList = getArguments().getParcelableArrayList(ARG_ITEMS_LIST);
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        calculateParamsOfThumb();

        //cache images
        if(BuildConfig.DEBUG) {
            //red corner - network, blue - disk, green - memory
            Picasso.with(getContext()).setIndicatorsEnabled(true);
//            Picasso.with(getContext()).setLoggingEnabled(true);
        }

//        for (Movie movie: moviesList) {
//            String thumbUrlString = movie.getThumbUrlString();
//            if (!TextUtils.isEmpty(thumbUrlString)) {
//                Picasso.with(getContext())
//                        .load("http://i.imgur.com/DvpvklR.png")
////                        .load(thumbUrlString)
//                        .fetch();
//            }
//        }
    }

    private void calculateParamsOfThumb() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageWidth = metrics.widthPixels / columnCount;
        imageHeight = Math.round(((float) imageWidth / 2) * 3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        if(view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (columnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
            rvAdapter = new MyMovieRecyclerViewAdapter(moviesList, listener, imageWidth, imageHeight);
            recyclerView.setAdapter(rvAdapter);
            recyclerView.setHasFixedSize(true);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesList();
    }

    private void updateMoviesList() {
        FetchMoviesTask weatherTask = new FetchMoviesTask();
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String locationPref = sharedPref.getString(getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
        weatherTask.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }



    public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private MoviesSource moviesSource = new DataSource(getContext()).getMoviesSource();

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String sortBy = "popularity.desc";

            try {
                // Construct the URL for themoviedb.org query
                // Possible parameters are available at http://openweathermap.org/API#forecast
                Uri builtUri = Uri.parse(Constants.MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.SORT_BY_PARAM, sortBy)
                        .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY_VALUE)
                        .build();
                URL url = new URL(builtUri.toString());
                Logger.d(LOG_TAG, "url: " + String.valueOf(url));

                // Create the request to themoviedb.org, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Logger.e(LOG_TAG, e);
                // If the code didn't successfully get the movie data, there's no point in attempting to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Logger.e(LOG_TAG, "while closing stream", e);
                    }
                }
            }

            try {
                List<Movie> moviesList = parseJson(forecastJsonStr);
                Logger.d(LOG_TAG, "successfully got JSON and parsed it! moviesList.size=" + String.valueOf(moviesList.size()));
                return moviesList;
            } catch (JSONException e) {
                Logger.e(LOG_TAG, e);
                e.printStackTrace();
            }

            Logger.e(LOG_TAG, "while getting json or parsing it");
            return null;
        }

        private List<Movie> parseJson(String JSONString) throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String results = "results";
            final String posterPath = "poster_path";
            final String id = "id";

            JSONObject serverAnswer = new JSONObject(JSONString);
            JSONArray moviesJsonArray = serverAnswer.getJSONArray(results);

            List<Movie> moviesList = new ArrayList<>();
            for(int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject JSONMovie = moviesJsonArray.getJSONObject(i);
                int movieID = JSONMovie.getInt(id);
                String moviePosterPath = JSONMovie.getString(posterPath);

                Movie oldMovie = moviesSource.getItemByID(movieID);
                if(oldMovie != null) {
                    oldMovie.setPosterPathUrlString(moviePosterPath);
                    moviesSource.update(oldMovie);
                    moviesList.add(oldMovie);
                } else {
                    Movie newMovie = new Movie(movieID, moviePosterPath);
                    moviesSource.create(newMovie);
                    moviesList.add(newMovie);
                }
            }
            return moviesList;
        }

        @Override
        protected void onPostExecute(List<Movie> moviesList) {
            super.onPostExecute(moviesList);
            rvAdapter.setList(moviesList);
            listener.dismissProgressBar();
        }
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie movie);
        void dismissProgressBar();
    }
}
