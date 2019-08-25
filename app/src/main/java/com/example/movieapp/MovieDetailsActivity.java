package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.Model.Movie;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.poster)
    ImageView poster;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.popularity)
    TextView popularity;
    @BindView(R.id.release_date)
    TextView date;
    @BindView(R.id.overview)
    TextView overview;

    private Movie m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        String Extra = getIntent().getStringExtra(Movie.class.getName());
        Gson gson = new Gson();
        m = gson.fromJson(Extra, Movie.class);
        setTitle(m.getOriginal_title());
        rating.setText("rating: "+m.getUser_rating() + "/10");
        popularity.setText("popularity: "+m.getPopularity() + "");
        date.setText("release date: "+m.getRelease_date());
        if (!m.getOverview().trim().equals("")) {
            overview.setText(m.getOverview());
        } else {
            overview.setText("No available data");
        }
        Picasso.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w342/" + m.getPoster_path())
                .error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_movie)
                .into(poster);
    }
}
