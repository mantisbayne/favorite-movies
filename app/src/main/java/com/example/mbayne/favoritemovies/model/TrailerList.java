package com.example.mbayne.favoritemovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Data model for list of trailers returned
 */

public class TrailerList {
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers = null;

    public List<Trailer> getTrailers() {
        return trailers;
    }
}
