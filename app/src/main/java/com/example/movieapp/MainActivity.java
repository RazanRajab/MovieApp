package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.movieapp.Adapters.MoviesAdapter;
import com.example.movieapp.Database.MyContentProvider;
import com.example.movieapp.Model.Movie;
import com.example.movieapp.TheMoviesAPI.MovieResponse;
import com.example.movieapp.TheMoviesAPI.MoviesService;
import com.example.movieapp.TheMoviesAPI.Result;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainRecyclerView) RecyclerView RecyclerView;
    private ArrayList<Movie> Movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("Movies");
        RecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        moviesAdapter = new MoviesAdapter(Movies);
        RecyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                Intent n = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                Gson gson = new Gson();
                n.putExtra(Movie.class.getName(), gson.toJson(Movies.get(position)));
                startActivity(n);
            }
        });
        if (savedInstanceState == null) {
            getPopularMovies();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                getPopularMovies();
                break;
            case R.id.top_rated:
                getTopRatedMovies();
                break;
            case R.id.favorite:
                getFavorites();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("MoviesList",Movies);
        super.onSaveInstanceState(outState, outPersistentState);
        Toast.makeText(getApplicationContext(),"onSavedInstanseState",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Movies = savedInstanceState.getParcelableArrayList("MoviesList");
        moviesAdapter.notifyDataSetChanged();
    }

    private void getPopularMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService service = retrofit.create(MoviesService.class);
        //Run the Request
        service.getPopular(Constants.YOUR_API_KEY)
                .enqueue(new Callback<MovieResponse>() {

                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                       convertResponseToMovies(response);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }
    private void convertResponseToMovies(Response<MovieResponse> response){
        Movies.clear();
        List<Result> results = response.body().getResults();
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getPoster_path() != null) {
                Movies.add(new Movie(results.get(i).getId(), results.get(i).getPoster_path(),
                        results.get(i).getOriginal_title(), results.get(i).getRelease_date(),
                        results.get(i).getOverview(), results.get(i).getPopularity(),
                        results.get(i).getUser_rating()));
            }
        }
        moviesAdapter.notifyDataSetChanged();
    }
    private void getTopRatedMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //status code header
        //200-300 successful
        //400 bad request
        //401/403 unauthorized/forbidden
        //500+ server error

        MoviesService service = retrofit.create(MoviesService.class);
        //Run the Request
        service.getTopRated(Constants.YOUR_API_KEY)
                .enqueue(new Callback<MovieResponse>() {

                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        convertResponseToMovies(response);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }
    public void getFavorites() {
        Uri uri = MyContentProvider.CONTENT_URI;
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);
        if (cursor != null) {
           Movies.clear();
            while (cursor.moveToNext()) {
                int id = cursor
                        .getInt(cursor.getColumnIndex(MyContentProvider.COLUMN_MOVIE_ID));
                String poster_url = cursor.
                        getString(cursor.getColumnIndex(MyContentProvider.COLUMN_POSTER));
                String title = cursor
                        .getString(cursor.getColumnIndex(MyContentProvider.COLUMN_TITLE));
                String release_date = cursor
                        .getString(cursor.getColumnIndex(MyContentProvider.COLUMN_RELEASE_DATE));
                String overview = cursor
                        .getString(cursor.getColumnIndex(MyContentProvider.COLUMN_OVERVIEW));
                double popularity = cursor
                        .getDouble(cursor.getColumnIndex(MyContentProvider.COLUMN_POPULARITY));
                double rating = cursor
                        .getDouble(cursor.getColumnIndex(MyContentProvider.COLUMN_RATING));
                Movie m = new Movie(id,poster_url,title,release_date,overview,popularity,rating);
                m.set_favorite(true);
                Movies.add(m);
            }
            cursor.close();
        }
        moviesAdapter.notifyDataSetChanged();
    }
}
