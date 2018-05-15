package com.example.mbayne.favoritemovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mbayne.favoritemovies.BuildConfig;
import com.example.mbayne.favoritemovies.R;
import com.example.mbayne.favoritemovies.adapter.MoviesAdapter;
import com.example.mbayne.favoritemovies.model.Movie;
import com.example.mbayne.favoritemovies.model.MovieList;
import com.example.mbayne.favoritemovies.rest.Client;
import com.example.mbayne.favoritemovies.rest.MoviesService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterClickListener {
    private static final String TAG = MovieListActivity.class.getSimpleName();
    private static final int SPAN_COUNT = 2;
    private static final String STATE_SORTED_BY = "sorted_by";
    public static final String EXTRA_MOVIE = "movie";

    @BindView(R.id.movies_error)
    TextView mErrorMessage;

    @BindView(R.id.movies_list)
    RecyclerView movieList;

    @BindView(R.id.loading_indicator)
    ProgressBar loading;

    MoviesAdapter adapter;
    SortType sortedBy;
    List<Movie> movies;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        movieList.setLayoutManager(layoutManager);
        movieList.setHasFixedSize(true);

        MoviesAdapter.MoviesAdapterClickListener listener = this::handleMovieItemClick;

        if (savedInstanceState == null)
            sortedBy = SortType.POPULAR;
        else
            sortedBy = (SortType) savedInstanceState.getSerializable(STATE_SORTED_BY);

        Call<MovieList> defaultCall = getSortByCall(sortedBy);

        loadMovies(listener, defaultCall);
    }

    private void loadMovies(MoviesAdapter.MoviesAdapterClickListener listener, Call<MovieList> call) {
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                movies = response.body().getResults();
                adapter = new MoviesAdapter(movies, R.layout.movie_list_item,
                        getApplicationContext(), listener);

                movieList.setAdapter(adapter);

                loading.setVisibility(View.GONE);
                movieList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.toString());
            }
        });
    }

    private Call<MovieList> getSortByCall(SortType type) {
        MoviesService service = Client.getMoviesClient().create(MoviesService.class);
        Call<MovieList> call = null;

        if (SortType.TOP_RATED.equals(type))
            call = service.getTopRatedMovies(BuildConfig.MOVIE_DB_API_KEY);
        else if (SortType.POPULAR.equals(type))
            call = service.getPopularMovies(BuildConfig.MOVIE_DB_API_KEY);

        return call;
    }

    private void handleMovieItemClick(int position) {
        movie = adapter.getItem(position);
        Intent detailsIntent = new Intent(this, MovieDetailsActivity.class);
        detailsIntent.putExtra(EXTRA_MOVIE, movie);
        startActivity(detailsIntent);
    }

    @Override
    public void onMovieItemClick(int position) {
        // not needed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.sort_by_rating) {
            loadMovies(this, getSortByCall(SortType.TOP_RATED));
            sortedBy = SortType.TOP_RATED;
            return true;
        } else if (itemId == R.id.sort_by_popularity) {
            loadMovies(this, getSortByCall(SortType.POPULAR));
            sortedBy = SortType.POPULAR;
            return true;
        } else if (itemId == R.id.show_favorites) {
            // TODO implement favorites
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private enum SortType {
        TOP_RATED,
        POPULAR,
        FAVORITES
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_SORTED_BY, sortedBy);
    }
}
