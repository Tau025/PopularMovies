package com.devtau.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Logger;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment {
    private static final String ARG_ITEMS_LIST = "itemsList";
    private static final String ARG_COLUMN_COUNT = "columnCount";
    private List<Movie> movieList;
    private int columnCount = 2;
    private OnListFragmentInteractionListener listener;
    private int imageWidth, imageHeight;

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

    public static MovieFragment newInstance(int columnCount, ArrayList<Movie> movieList) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ITEMS_LIST, movieList);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            movieList = getArguments().getParcelableArrayList(ARG_ITEMS_LIST);
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        calculateParamsOfThumb();

        //cache images
        if(BuildConfig.DEBUG) {
            //red corner - network, blue - disk, green - memory
            Picasso.with(getContext()).setIndicatorsEnabled(true);
//            Picasso.with(getContext()).setLoggingEnabled(true);
        }

//        for (Movie movie: movieList) {
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
            recyclerView.setAdapter(new MyMovieRecyclerViewAdapter(movieList, listener, imageWidth, imageHeight));
            recyclerView.setHasFixedSize(true);
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie movie);
    }
}
