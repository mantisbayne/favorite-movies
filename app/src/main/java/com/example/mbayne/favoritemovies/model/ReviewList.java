package com.example.mbayne.favoritemovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Data model for list of reviews for a movie
 */

public class ReviewList {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("page")
    @Expose
    private Integer page;

    @SerializedName("results")
    @Expose
    private List<Review> results = null;

    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalReviews;

    public ReviewList(Integer id, Integer page, List<Review> results, Integer totalPages, Integer totalReviews) {
        this.id = id;
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalReviews = totalReviews;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPage() {
        return page;
    }

    public List<Review> getReviews() {
        return results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }
}
