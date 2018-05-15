package com.example.mbayne.favoritemovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbayne.favoritemovies.Constants;
import com.example.mbayne.favoritemovies.R;
import com.example.mbayne.favoritemovies.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private Context context;
    private List<Trailer> trailerList;
    private static final String TRAILER_BASE_URL = Constants.TRAILER_BASE_URL;

    public TrailerAdapter(Context context, List<Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        final Trailer trailer = trailerList.get(position);

        holder.trailerTitle.setText(trailer.getName());
        holder.playButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(TRAILER_BASE_URL + trailer.getKey()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView playButton;
        TextView trailerTitle;

        TrailerViewHolder(View itemView) {
            super(itemView);

            playButton = itemView.findViewById(R.id.trailer_play_button);
            trailerTitle = itemView.findViewById(R.id.trailer_title);
        }
    }
}
