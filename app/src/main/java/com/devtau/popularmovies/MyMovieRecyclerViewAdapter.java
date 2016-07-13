package com.devtau.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.devtau.popularmovies.MovieFragment.OnListFragmentInteractionListener;
import com.devtau.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder> {
    private final List<Movie> movieList;
    private final OnListFragmentInteractionListener mListener;
    private int imageWidth;
    private int imageHeight;

    public MyMovieRecyclerViewAdapter(List<Movie> movieList, OnListFragmentInteractionListener listener,
                                      int imageWidth, int imageHeight) {
        this.movieList = movieList;
        mListener = listener;
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
        holder.movie = movieList.get(position);
        String thumbUrlString = holder.movie.getThumbUrlString();
//        if (!TextUtils.isEmpty(thumbUrlString)) {}

        loadImage(holder);

        holder.view.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.movie);
            }
        });
    }

    private void loadImage(ViewHolder holder) {
        Picasso.with(holder.view.getContext())
                .load("http://kogteto4ka.ru/wp-content/uploads/2012/04/%D0%9A%D0%BE%D1%82%D0%B5%D0%BD%D0%BE%D0%BA.jpg")
//                .load("http://i.imgur.com/DvpvklR.png")
//                .load(thumbUrlString)
                .placeholder(R.mipmap.ic_launcher)
                .error(android.R.drawable.ic_dialog_alert)
                .centerCrop()
                .resize(imageWidth, imageHeight)
                .into(holder.movieThumb);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
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
