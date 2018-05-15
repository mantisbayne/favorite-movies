package com.example.mbayne.favoritemovies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbayne.favoritemovies.BuildConfig;
import com.example.mbayne.favoritemovies.R;
import com.example.mbayne.favoritemovies.adapter.MoviesAdapter;
import com.example.mbayne.favoritemovies.adapter.TrailerAdapter;
import com.example.mbayne.favoritemovies.model.Movie;
import com.example.mbayne.favoritemovies.model.MovieList;
import com.example.mbayne.favoritemovies.model.ReviewList;
import com.example.mbayne.favoritemovies.model.Trailer;
import com.example.mbayne.favoritemovies.model.TrailerList;
import com.example.mbayne.favoritemovies.rest.Client;
import com.example.mbayne.favoritemovies.rest.MoviesService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

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

        favoriteIcon.setOnClickListener(view -> addToFavorites());

        MoviesService service = Client.getMoviesClient().create(MoviesService.class);

        loadTrailers(service);
    }

    private void loadReviews(MoviesService service) {
        Call<ReviewList> call = service.getReviews(movie.getId(), BuildConfig.MOVIE_DB_API_KEY);
        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {

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
                List<Trailer> trailerList = response.body().getTrailers();
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

    private void addToFavorites() {

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
        
    }
}
