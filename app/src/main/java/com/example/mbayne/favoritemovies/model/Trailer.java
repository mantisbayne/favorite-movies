package com.example.mbayne.favoritemovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data Model for a movie trailer
 */

public class Trailer {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("site")
    @Expose
    private String site;

    @SerializedName("size")
    @Expose
    private Integer size;

    @SerializedName("type")
    @Expose
    private String type;

    public Trailer(String id, String key, String name, String site, Integer size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
