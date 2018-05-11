package com.example.mbayne.favoritemovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mbayne.favoritemovies.R;
import com.example.mbayne.favoritemovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>  {
    private List<Movie> movies;
    private int itemLayout;
    private Context context;
    private MoviesAdapterClickListener listener;


    public interface MoviesAdapterClickListener {
        void onMovieItemClick(int position);
    }

    public MoviesAdapter(List<Movie> movies, int itemLayout, Context context, MoviesAdapterClickListener listener) {
        this.movies = movies;
        this.itemLayout = itemLayout;
        this.context = context;
        this.listener = listener;
    }

    @Override
    @NonNull
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.build().load(movie.getPosterPath())
                .noFade()
                .placeholder(R.drawable.ic_movie_black)
                .error(R.drawable.ic_error_outline_black)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        }
        return movies.size();
    }

    public Movie getItem(int id) {
        return movies == null ? null : movies.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster)
        ImageView mImageView;

        private ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mImageView.setOnClickListener(v -> listener.onMovieItemClick(getAdapterPosition()));
        }
    }
}