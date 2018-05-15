package com.example.mbayne.favoritemovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbayne.favoritemovies.R;
import com.example.mbayne.favoritemovies.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviewList = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        final Review review = reviewList.get(position);

        holder.mAuthor.setText(review.getAuthor());
        holder.mDescription.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mAuthor;
        TextView mDescription;

        ReviewViewHolder(View itemView) {
            super(itemView);

            mAuthor = itemView.findViewById(R.id.movie_review_author);
            mDescription = itemView.findViewById(R.id.movie_review_description);
        }
    }
}
