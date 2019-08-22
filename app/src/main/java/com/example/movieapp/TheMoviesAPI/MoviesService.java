package com.example.movieapp.TheMoviesAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesService {

    //https://api.themoviedb.org/3
    @GET("/movie/popular")
    Call<MovieResponse> getPopualr(@Query("api_key") String token);

    @GET("/movie/top_rated")
    Call<MovieResponse> getTopRated(@Query("api_key") String token);
}
