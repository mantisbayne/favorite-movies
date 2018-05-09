package com.example.mbayne.favoritemovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for a movie review
 */

public class Review {

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("url")
    @Expose
    private String url;

    public Review(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
