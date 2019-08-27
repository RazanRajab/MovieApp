package com.example.movieapp.TheMoviesAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {

    @SerializedName("results")
    List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }
}
