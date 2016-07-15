package com.devtau.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.devtau.popularmovies.fragments.MovieFragment.OnListFragmentInteractionListener;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Util;
import java.util.List;

public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder> {
    private List<Movie> moviesList;
    private final OnListFragmentInteractionListener listener;
    private int imageWidth;
    private int imageHeight;

    public MyMovieRecyclerViewAdapter(List<Movie> moviesList, OnListFragmentInteractionListener listener,
                                      int imageWidth, int imageHeight) {
        this.moviesList = moviesList;
        this.listener = listener;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.movie = moviesList.get(position);
        Util.loadImageToView(holder.view.getContext(), holder.movie.getPosterPath(), holder.movieThumb,
                imageWidth, imageHeight);

        holder.view.setOnClickListener(v -> {
            if (null != listener) {
                listener.onListFragmentInteraction(holder.movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setList(List<Movie> moviesList){
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView movieThumb;
        public Movie movie;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            movieThumb = (ImageView) view.findViewById(R.id.movie_thumb);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + movieThumb.getContentDescription() + "'";
        }
    }
}
