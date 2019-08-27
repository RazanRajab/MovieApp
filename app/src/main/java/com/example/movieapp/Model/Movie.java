package com.example.movieapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private int id;
    private String poster_path;
    private String original_title;
    private String release_date;
    private String overview;
    private Double popularity;
    private Double user_rating;
    private boolean is_favorite;

    public Movie(int id, String poster_path, String original_title, String release_date,
                 String overview, Double popularity, Double user_rating) {
        this.id = id;
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.release_date = release_date;
        this.overview = overview;
        this.popularity = popularity;
        this.user_rating = user_rating;
        this.is_favorite = false;
    }

    public Movie(Parcel p){
        id=p.readInt();
        poster_path=p.readString();
        original_title=p.readString();
        release_date=p.readString();
        overview=p.readString();
        popularity=p.readDouble();
        user_rating=p.readDouble();
        is_favorite=p.readBoolean();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(poster_path);
        parcel.writeString(original_title);
        parcel.writeString(release_date);
        parcel.writeString(overview);
        parcel.writeDouble(popularity);
        parcel.writeDouble(user_rating);
        parcel.writeValue(is_favorite);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Double getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(Double user_rating) {
        this.user_rating = user_rating;
    }

    public boolean Is_favorite() {
        return is_favorite;
    }

    public void set_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }
}
