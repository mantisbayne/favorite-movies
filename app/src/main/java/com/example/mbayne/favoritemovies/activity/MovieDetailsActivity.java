package com.example.mbayne.favoritemovies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbayne.favoritemovies.R;
import com.example.mbayne.favoritemovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Movie movie = extras.getParcelable(MovieListActivity.EXTRA_MOVIE);
            if (movie != null) {
                title.setText(movie.getTitle());
                loadPoster(movie.getPosterPath(), poster);
                releaseDate.setText(movie.getReleaseDate());
                rating.setText(movie.getRating());
                overview.setText(movie.getOverview());
            }
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
}
