package com.devtau.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import com.devtau.popularmovies.database.tables.MoviesTable;
import com.devtau.popularmovies.util.Util;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import static com.devtau.popularmovies.database.tables.MoviesTable.*;
/**
 * Класс описывает пример хранимого в клиенте, для которого нужно списковое представление
 * класс хранимого объекта должен:
 * 1 - переопределить методы equals() и hashCode() - для корректного удаления элемента из списка
 * 2 - реализовать Parcelable
 */
public class Movie implements Parcelable{
    private long id;
    private Calendar date;
    private int price;
    private String description;
    private String thumbUrlString;

    public Movie(Calendar date, int price, String description) {
        this.id = -1;//необходимо для использования автоинкремента id новой записи в sql
        this.date = date;
        this.price = price;
        this.description = description;
    }

    public Movie(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        date = null;
        try {
            date = Calendar.getInstance();
            String dateString = cursor.getString(cursor.getColumnIndex(MoviesTable.DATE));
            date.setTime(Util.dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        price = cursor.getInt(cursor.getColumnIndex(PRICE));
        description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
    }

    private Movie(Parcel parcel) {
        id = parcel.readLong();
        date = Calendar.getInstance();
        date.setTimeInMillis(parcel.readLong());
        price = parcel.readInt();
        description = parcel.readString();
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

    public void update(Calendar date, int price, String description) {
        this.date = date;
        this.price = price;
        this.description = description;
    }


    //геттеры
    public long getId() {
        return id;
    }

    public Calendar getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }


    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", date=" + Util.dateFormat.format(date.getTime()) +
                ", price=" + price +
                ", description='" + description + '\'' +
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
        parcel.writeLong(date.getTimeInMillis());
        parcel.writeInt(price);
        parcel.writeString(description);
    }

    public String getThumbUrlString() {
        return thumbUrlString;
    }
}
