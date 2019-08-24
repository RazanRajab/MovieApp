package com.example.movieapp.Model;

public class Movie {
    private int id;
    private String poster_path;
    private String original_title;
    private String release_date;
    private String overview;
    private Double popularity;
    private Double user_rating;

    public Movie(int id, String poster_path, String original_title, String release_date,
                 String overview, Double popularity, Double user_rating) {
        this.id = id;
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.release_date = release_date;
        this.overview = overview;
        this.popularity = popularity;
        this.user_rating = user_rating;
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
}
