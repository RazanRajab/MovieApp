package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.Adapters.MoviesAdapter;
import com.example.movieapp.Adapters.ReviewsAdapter;
import com.example.movieapp.Adapters.TrailersAdapter;
import com.example.movieapp.Database.MyContentProvider;
import com.example.movieapp.Model.Movie;
import com.example.movieapp.TheMoviesAPI.MoviesService;
import com.example.movieapp.TheMoviesAPI.Review;
import com.example.movieapp.TheMoviesAPI.ReviewsResponse;
import com.example.movieapp.TheMoviesAPI.Trailer;
import com.example.movieapp.TheMoviesAPI.TrailersResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.reviewsRecycler)
    androidx.recyclerview.widget.RecyclerView RecyclerView;
    @BindView(R.id.trailersRecycler)
    androidx.recyclerview.widget.RecyclerView trailersRecyclerView;
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
    @BindView(R.id.add_favorite)
    Button Favorite;

    private ArrayList<Review> reviews = new ArrayList<>();
    private ReviewsAdapter reviewsAdapter;
    private ArrayList<Trailer> trailers = new ArrayList<>();
    private TrailersAdapter trailersAdapter;
    private Movie m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false));
        reviewsAdapter = new ReviewsAdapter(reviews);
        RecyclerView.setAdapter(reviewsAdapter);
        reviewsAdapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                openReview(reviews.get(position));
            }
        });
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false));
        trailersAdapter = new TrailersAdapter(trailers);
        trailersRecyclerView.setAdapter(trailersAdapter);
        trailersAdapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                openVideo(trailers.get(position));
            }
        });
        String Extra = getIntent().getStringExtra(Movie.class.getName());
        Gson gson = new Gson();
        if(savedInstanceState==null) {
            m = gson.fromJson(Extra, Movie.class);
            getReviews();
            getTrailers();
        }
        setTitle(m.getOriginal_title());
        rating.setText("rating: "+m.getUser_rating() + "/10");
        popularity.setText("popularity: "+m.getPopularity() + "");
        date.setText("release date: "+m.getRelease_date());
        if(m.Is_favorite()){
            Favorite.setText("Remove from Favorite");
        }
        if (!m.getOverview().trim().equals("")) {
            overview.setText(m.getOverview());
        } else {
            overview.setText("No available data");
        }
        Picasso.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w342/" + m.getPoster_path())
                .error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_movie)
                .into(poster);
        Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m.Is_favorite()){
                    Favorite.setText("Add to Favorite");
                    m.set_favorite(false);
                    //remove from database
                    UnFavoriteMovie(m);
                }
                else {
                    Favorite.setText("Remove from Favorite");
                    m.set_favorite(true);
                    //add to database
                    FavoriteMovie(m);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("reviews", reviews);
        outState.putParcelableArrayList("trailers", trailers);
        outState.putParcelable("movie",m);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        reviews = savedInstanceState.getParcelableArrayList("reviews");
        trailers = savedInstanceState.getParcelableArrayList("trailers");
        m = savedInstanceState.getParcelable("movie");
        reviewsAdapter.notifyDataSetChanged();
        trailersAdapter.notifyDataSetChanged();
    }

    private void FavoriteMovie(Movie m) {
        ContentValues Values = new ContentValues();
        Values.put(MyContentProvider.COLUMN_MOVIE_ID, m.getId());
        Values.put(MyContentProvider.COLUMN_POSTER, m.getPoster_path());
        Values.put(MyContentProvider.COLUMN_TITLE, m.getOriginal_title());
        Values.put(MyContentProvider.COLUMN_OVERVIEW, m.getOverview());
        Values.put(MyContentProvider.COLUMN_RELEASE_DATE, m.getRelease_date());
        Values.put(MyContentProvider.COLUMN_POPULARITY, m.getPopularity());
        Values.put(MyContentProvider.COLUMN_RATING, m.getUser_rating());
        getContentResolver().insert(MyContentProvider.CONTENT_URI, Values);
        Toast.makeText(MovieDetailsActivity.this,
                "Added To Favorites", Toast.LENGTH_SHORT).show();
    }

    private void UnFavoriteMovie(Movie m) {
        getContentResolver().delete(MyContentProvider.CONTENT_URI,
                MyContentProvider.COLUMN_MOVIE_ID + " = " + m.getId(), null);
        Toast.makeText(MovieDetailsActivity.this,
                "Removed from Favorites", Toast.LENGTH_SHORT).show();
    }

    private void openReview(Review r) {
        Intent n= new Intent(Intent.ACTION_VIEW, Uri.parse(r.getUrl()));
        startActivity(n);
    }

    private void openVideo(Trailer t){
        Intent n= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+
                t.getKey()));
        startActivity(n);
    }

    private void getReviews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService service = retrofit.create(MoviesService.class);
        service.getReviews(m.getId(),Constants.YOUR_API_KEY).enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<Review> r = response.body().getReviews();

                for (int i = 0; i <r.size();i++) {
                    reviews.add(new Review(r.get(i).getContent(),r.get(i).getAuthor(),r.get(i).getUrl()));
                }
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {

            }
        });
    }

    private void getTrailers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService service = retrofit.create(MoviesService.class);
        service.getTrailers(m.getId(),Constants.YOUR_API_KEY).enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                List<Trailer> t = response.body().getTrailers();

                for (int i = 0; i <t.size();i++) {
                    trailers.add(new Trailer(t.get(i).getKey(),t.get(i).getName()));
                }
                trailersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {

            }
        });
    }
}
