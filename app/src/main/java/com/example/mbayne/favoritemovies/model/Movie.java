package com.example.mbayne.favoritemovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.Date;

/**
 * Movie object representation of response from server
 */

public class Movie implements Parcelable {
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";

    @SerializedName("id")
    private int id;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("vote_average")
    private Double voteAverage;

    /**
     * Implementation of a Movie object
     * @param releaseDate - date film was released to public
     * @param posterPath - path to image file for movie poster
     * @param originalTitle - title of film in its original language
     * @param voteAverage - rating based on user votes
     * @param overview - plot summary for the film
     */
    public Movie(int id, String originalTitle, String overview,
                   String posterPath, Date releaseDate, Double voteAverage) {
        super();

        this.id = id;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        long tmpDate = in.readLong();
        releaseDate = tmpDate == -1 ? null : new Date(tmpDate);
        voteAverage = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return POSTER_BASE_URL + posterPath;
    }

    public String formatDate(DateFormat dateFormat) {
        return dateFormat.format(releaseDate);
    }

    public String formatVoteAverage() {
        return voteAverage.toString() + "/10";
    }

    public int getId() {
        return id;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeLong(releaseDate != null ? releaseDate.getTime() : -1);
        dest.writeDouble(voteAverage);
    }
}
