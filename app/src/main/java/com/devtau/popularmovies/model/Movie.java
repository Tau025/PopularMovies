package com.devtau.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import com.devtau.popularmovies.util.Util;
import java.text.ParseException;
import java.util.Calendar;
import static com.devtau.popularmovies.database.tables.MoviesTable.*;

public class Movie implements Parcelable{
    private long id;
    private String title;
    private String posterPath;
    private String plotSynopsis;
    private float userRating;
    private Calendar releaseDate = Calendar.getInstance();

    public Movie(int id, String posterPath) {
        this.id = id;
        this.posterPath = posterPath;
    }

    public Movie(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        title = cursor.getString(cursor.getColumnIndex(TITLE));
        posterPath = cursor.getString(cursor.getColumnIndex(POSTER_PATH));
        plotSynopsis = cursor.getString(cursor.getColumnIndex(PLOT_SYNOPSIS));
        userRating = 0f;
        try {
            releaseDate = Calendar.getInstance();
            String dateString = cursor.getString(cursor.getColumnIndex(RELEASE_DATE));
            releaseDate.setTime(Util.dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Movie(Parcel parcel) {
        id = parcel.readLong();
        title = parcel.readString();
        posterPath = parcel.readString();
        plotSynopsis = parcel.readString();
        userRating = parcel.readFloat();
        releaseDate = Calendar.getInstance();
        releaseDate.setTimeInMillis(parcel.readLong());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass() || id == -1) return false;
        Movie that = (Movie) obj;
        if (that.id == -1) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id != -1 ? 31 * id : 0);
    }



    //setters
    public void setId(long id) {
        this.id = id;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }



    //getters
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public float getUserRating() {
        return userRating;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", userRating=" + userRating +
                ", releaseDate=" + Util.dateFormat.format(releaseDate.getTime()) +
                '}';
    }

    //Parcelable methods
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(plotSynopsis);
        parcel.writeFloat(userRating);
        parcel.writeLong(releaseDate.getTimeInMillis());
    }
}
