package com.devtau.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import com.devtau.popularmovies.util.Logger;
import com.devtau.popularmovies.util.Util;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static com.devtau.popularmovies.database.tables.MoviesTable.*;

public class Movie implements Parcelable{
    private final String LOG_TAG = Movie.class.getSimpleName();
    private long id;
    private String title;
    private String posterPath;
    private String plotSynopsis;
    private double userRating;
    private Calendar releaseDate = new GregorianCalendar(1970, 0, 1);

    public Movie(long id, String title, String posterPath, String plotSynopsis, double userRating, Calendar releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public Movie(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        title = cursor.getString(cursor.getColumnIndex(TITLE));
        posterPath = cursor.getString(cursor.getColumnIndex(POSTER_PATH));
        plotSynopsis = cursor.getString(cursor.getColumnIndex(PLOT_SYNOPSIS));
        userRating = 0d;
        try {
            releaseDate = new GregorianCalendar(1970, 0, 1);
            String dateString = cursor.getString(cursor.getColumnIndex(RELEASE_DATE));
            releaseDate.setTime(Util.dateFormat.parse(dateString));
        } catch (ParseException e) {
            Logger.e(LOG_TAG, "while parsing releaseDate from Cursor", e);
            e.printStackTrace();
        }
    }

    private Movie(Parcel parcel) {
        id = parcel.readLong();
        title = parcel.readString();
        posterPath = parcel.readString();
        plotSynopsis = parcel.readString();
        userRating = parcel.readDouble();
        releaseDate = new GregorianCalendar(1970, 0, 1);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
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

    public double getUserRating() {
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
        parcel.writeDouble(userRating);
        parcel.writeLong(releaseDate.getTimeInMillis());
    }
}
