package com.devtau.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.devtau.popularmovies.MovieFragment.OnListFragmentInteractionListener;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Constants;
import com.devtau.popularmovies.util.Logger;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder> {
    private final String LOG_TAG = MyMovieRecyclerViewAdapter.class.getSimpleName();
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
        loadImage(holder);

        holder.view.setOnClickListener(v -> {
            if (null != listener) {
                listener.onListFragmentInteraction(holder.movie);
            }
        });
    }

    private void loadImage(ViewHolder holder) {
        String posterFullUrl = holder.movie.getPosterPathUrlString();
        if (TextUtils.isEmpty(posterFullUrl) || "".equals(posterFullUrl)) {
            Logger.e(LOG_TAG, "No posterUrlString found in movie. Replacing with Kitty");
            posterFullUrl = "http://kogteto4ka.ru/wp-content/uploads/2012/04/%D0%9A%D0%BE%D1%82%D0%B5%D0%BD%D0%BE%D0%BA.jpg";
        } else {
            posterFullUrl = Constants.IMAGE_STORAGE_BASE_URL + Constants.POSTER_SIZE + posterFullUrl;
        }
        Picasso.with(holder.view.getContext())
                .load(posterFullUrl)
                .error(R.drawable.load_failed)
                .centerCrop()
                .resize(imageWidth, imageHeight)
                .into(holder.movieThumb);
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
