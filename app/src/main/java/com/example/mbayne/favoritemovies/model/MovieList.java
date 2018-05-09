package com.example.mbayne.favoritemovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Implementation list of movies object
 */

public class MovieList {
    @SerializedName("results")
    private ArrayList<Movie> movies = null;

    public ArrayList<Movie> getResults() {
        return movies;
    }
}
