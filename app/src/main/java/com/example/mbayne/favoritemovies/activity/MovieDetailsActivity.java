package com.example.mbayne.favoritemovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbayne.favoritemovies.BuildConfig;
import com.example.mbayne.favoritemovies.Constants;
import com.example.mbayne.favoritemovies.R;
import com.example.mbayne.favoritemovies.adapter.ReviewAdapter;
import com.example.mbayne.favoritemovies.adapter.TrailerAdapter;
import com.example.mbayne.favoritemovies.data.MovieContract;
import com.example.mbayne.favoritemovies.model.Movie;
import com.example.mbayne.favoritemovies.model.Review;
import com.example.mbayne.favoritemovies.model.ReviewList;
import com.example.mbayne.favoritemovies.model.Trailer;
import com.example.mbayne.favoritemovies.model.TrailerList;
import com.example.mbayne.favoritemovies.rest.Client;
import com.example.mbayne.favoritemovies.rest.MoviesService;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String STATE_FAVORITE = "favorite";

    @BindView(R.id.movie_details_title)
    TextView title;
    @BindView(R.id.movie_details_poster)
    ImageView poster;
    @BindView(R.id.movie_details_release_date)
    TextView releaseDate;
    @BindView(R.id.movie_details_rating)
    TextView rating;
    @BindView(R.id.movie_details_overview)
    TextView overview;
    @BindView(R.id.trailers_list)
    RecyclerView trailers;
    @BindView(R.id.reviews_list)
    RecyclerView reviews;
    @BindView(R.id.rating_bar)
    MaterialRatingBar ratingBar;
    @BindView(R.id.favorite_movie_icon)
    ImageView favoriteIcon;

    private Movie movie;
    List<Trailer> trailerList;
    private boolean isFavorite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            movie = extras.getParcelable(MovieListActivity.EXTRA_MOVIE);
            if (movie != null) {
                title.setText(movie.getTitle());
                loadPoster(movie.getPosterPath(), poster);
                releaseDate.setText(movie.getReleaseDate());
                rating.setText(movie.getRating());
                overview.setText(movie.getOverview());
                ratingBar.setRating((float) (movie.getVoteAverage() / 2));
            }
        }

        if (savedInstanceState != null)
            isFavorite = savedInstanceState.getBoolean(STATE_FAVORITE);
        else
            isFavorite = isFavorite();

        if (isFavorite)
            favoriteIcon.setImageResource(R.drawable.ic_favorite);
        else
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border);

        favoriteIcon.setOnClickListener(view -> toggleFavorite());

        MoviesService service = Client.getMoviesClient().create(MoviesService.class);

        loadTrailers(service);
        loadReviews(service);
    }

    private void loadReviews(MoviesService service) {
        Call<ReviewList> call = service.getReviews(movie.getId(), BuildConfig.MOVIE_DB_API_KEY);
        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                List<Review> reviewList = response.body().getReviews();
                if (reviewList.size() > 0) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(reviews.getContext(),
                            LinearLayoutManager.VERTICAL, false);
                    reviews.setLayoutManager(layoutManager);
                    reviews.setHasFixedSize(true);
                    reviews.setAdapter(new ReviewAdapter(reviews.getContext(), reviewList));
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.toString());
            }
        });
    }

    private void loadTrailers(MoviesService service) {
        Call<TrailerList> call = service.getTrailers(movie.getId(), BuildConfig.MOVIE_DB_API_KEY);
        call.enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                trailerList = response.body().getTrailers();
                if (trailerList.size() > 0) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(trailers.getContext(),
                            LinearLayoutManager.HORIZONTAL, false);
                    trailers.setLayoutManager(layoutManager);
                    trailers.setHasFixedSize(true);
                    trailers.setAdapter(new TrailerAdapter(trailers.getContext(), trailerList));
                }
            }

            @Override
            public void onFailure(Call<TrailerList> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.toString());
            }
        });
    }

    private boolean isFavorite() {
        final Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                "movie_id=?",
                new String[]{String.valueOf(movie.getId())},
                null);

        return cursor != null && cursor.getCount() > 0;
    }

    private void toggleFavorite() {
        updateFavoriteView();

        if (isFavorite) {
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(String.valueOf(movie.getId())).build();
            getContentResolver().delete(uri, null, null);
            getContentResolver().notifyChange(uri, null);
            Toast.makeText(getApplicationContext(), movie.getTitle()
                    + " has been removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                Toast.makeText(getApplicationContext(), movie.getTitle()
                        + " has been added to favorites", Toast.LENGTH_SHORT).show();
            }
        }

        isFavorite = isFavorite();
    }

    private void updateFavoriteView() {
        if (!isFavorite) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite);
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void loadPoster(String posterPath, ImageView poster) {
        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        builder.build().load(posterPath)
                .noFade()
                .placeholder(R.drawable.ic_movie_black)
                .error(R.drawable.ic_error_outline_black)
                .into(poster);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_movie)
            shareMovie();
        return super.onOptionsItemSelected(item);
    }

    private void shareMovie() {
        Trailer trailer = trailerList.get(0);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, trailer.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.TRAILER_BASE_URL + trailer.getKey());
        startActivity(shareIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_FAVORITE, isFavorite);
    }
}
