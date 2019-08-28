package com.example.movieapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private int id;
    private String posterPath;
    private String originalTitle;
    private String releaseDate;
    private String overview;
    private Double popularity;
    private Double userRating;
    private boolean isFavorite;

    public Movie(int id, String poster_path, String original_title, String release_date,
                 String overview, Double popularity, Double user_rating) {
        this.id = id;
        this.posterPath = poster_path;
        this.originalTitle = original_title;
        this.releaseDate = release_date;
        this.overview = overview;
        this.popularity = popularity;
        this.userRating = user_rating;
        this.isFavorite = false;
    }

    public Movie(Parcel p){
        id=p.readInt();
        posterPath=p.readString();
        originalTitle=p.readString();
        releaseDate=p.readString();
        overview=p.readString();
        popularity=p.readDouble();
        userRating=p.readDouble();
        isFavorite=p.readBoolean();
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
        parcel.writeString(posterPath);
        parcel.writeString(originalTitle);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeDouble(popularity);
        parcel.writeDouble(userRating);
        parcel.writeValue(isFavorite);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
