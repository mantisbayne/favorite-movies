package com.example.mbayne.favoritemovies.activity;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.example.mbayne.favoritemovies.BuildConfig;
import com.example.mbayne.favoritemovies.R;
import com.example.mbayne.favoritemovies.Utils;
import com.example.mbayne.favoritemovies.adapter.MoviesAdapter;
import com.example.mbayne.favoritemovies.data.MovieContract;
import com.example.mbayne.favoritemovies.model.Movie;
import com.example.mbayne.favoritemovies.model.MovieList;
import com.example.mbayne.favoritemovies.rest.Client;
import com.example.mbayne.favoritemovies.rest.MoviesService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterClickListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {
    private static final String TAG = MovieListActivity.class.getSimpleName();
    private static final int SPAN_COUNT = 2;
    private static final String STATE_SORTED_BY = "sorted_by";
    public static final String EXTRA_MOVIE = "movie";
    private static final int MOVIE_LOADER_ID = 8;

    @BindView(R.id.movies_error)
    TextView errorMessage;
    @BindView(R.id.movies_list)
    RecyclerView movieList;
    @BindView(R.id.favorites_empty)
    TextView emptyFavorites;
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

        if (savedInstanceState == null)
            sortedBy = SortType.POPULAR;
        else
            sortedBy = (SortType) savedInstanceState.getSerializable(STATE_SORTED_BY);
        Context context = getApplicationContext();

        if (Utils.isNetworkAvailable(context)) {
            Call<MovieList> defaultCall = getSortByCall(sortedBy);
            loadMovies(defaultCall);
        } else {
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            Toast.makeText(context, R.string.no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMovies(Call<MovieList> call) {
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                movies = response.body().getResults();

                if (movies == null) {
                    movieList.setVisibility(View.GONE);
                    errorMessage.setVisibility(View.VISIBLE);
                }

                adapter = new MoviesAdapter(movies, R.layout.movie_list_item,
                        getApplicationContext(), position -> {
                            handleMovieItemClick(position);
                        });
                movieList.setAdapter(adapter);

                loading.setVisibility(View.GONE);
                movieList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
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

    protected void handleMovieItemClick(int position) {
        movie = adapter.getItem(position);
        Intent detailsIntent = new Intent(this, MovieDetailsActivity.class);
        detailsIntent.putExtra(EXTRA_MOVIE, movie);
        startActivity(detailsIntent);
    }

    @Override
    public void onMovieItemClick(int position) {
        handleMovieItemClick(position);
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
            loadMovies(getSortByCall(SortType.TOP_RATED));
            sortedBy = SortType.TOP_RATED;
            return true;
        } else if (itemId == R.id.sort_by_popularity) {
            loadMovies(getSortByCall(SortType.POPULAR));
            return true;
        } else if (itemId == R.id.show_favorites) {
            loading.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> movies = null;

            @Override
            protected void onStartLoading() {
                if (movies != null)
                    deliverResult(movies);
                else
                    forceLoad();
            }

            @Override
            public List<Movie> loadInBackground() {
                try {
                    Cursor cursor = getContext().getContentResolver().query(
                            MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                    if (cursor == null) {
                        return null;
                    }

                    movies = convertCursorToMovieList(cursor);
                    return movies;
                } catch (Exception e) {
                    movieList.setVisibility(View.GONE);
                    errorMessage.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<Movie> data) {
                movies = data;
                super.deliverResult(data);
            }

            private List<Movie> convertCursorToMovieList(@NonNull Cursor cursor) {
                int numRows = cursor.getCount();

                if (numRows == 0) {
                    return new ArrayList<>(0);
                }

                List<Movie> movies = new ArrayList<>(numRows);

                int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
                int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                int overviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
                int posterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
                int dateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                int voteAverageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Integer id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String overview = cursor.getString(overviewIndex);
                    String posterPath = cursor.getString(posterPathIndex);
                    String releaseDate = cursor.getString(dateIndex);
                    Double voteAverage = cursor.getDouble(voteAverageIndex);

                    movies.add(new Movie(posterPath, overview, releaseDate, id, title, voteAverage));
                }

                return movies;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        loading.setVisibility(View.GONE);
        if (movies == null || movies.size() == 0) {
            emptyFavorites.setVisibility(View.VISIBLE);
            movieList.setVisibility(View.GONE);
        } else {
            adapter.setMovieData(movies);
            movieList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // not used
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
