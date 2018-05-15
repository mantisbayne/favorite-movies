package com.example.mbayne.favoritemovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

import static com.example.mbayne.favoritemovies.Constants.POSTER_BASE_URL;

/**
 * Movie object representation of response from server
 */

public class Movie implements Parcelable {
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;

    public Movie(String posterPath, String overview, String releaseDate, Integer id,
                 String originalTitle, String title, Double popularity, Integer voteCount,
                 Boolean video, Double voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {

        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        originalTitle = in.readString();
        title = in.readString();
        if (in.readByte() == 0) {
            popularity = null;
        } else {
            popularity = in.readDouble();
        }
        if (in.readByte() == 0) {
            voteCount = null;
        } else {
            voteCount = in.readInt();
        }
        byte tmpVideo = in.readByte();
        video = tmpVideo == 0 ? null : tmpVideo == 1;
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(originalTitle);
        dest.writeString(title);
        if (popularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(popularity);
        }
        if (voteCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(voteCount);
        }
        dest.writeByte((byte) (video == null ? 0 : video ? 1 : 2));
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(voteAverage);
        }
    }

    public String getPosterPath() {
        return POSTER_BASE_URL + posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate.substring(0, releaseDate.indexOf('-'));
    }

    public Integer getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getRating() {
        return voteAverage.toString() + "/10";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(posterPath, movie.posterPath) &&
                Objects.equals(overview, movie.overview) &&
                Objects.equals(releaseDate, movie.releaseDate) &&
                Objects.equals(id, movie.id) &&
                Objects.equals(originalTitle, movie.originalTitle) &&
                Objects.equals(title, movie.title) &&
                Objects.equals(popularity, movie.popularity) &&
                Objects.equals(voteCount, movie.voteCount) &&
                Objects.equals(video, movie.video) &&
                Objects.equals(voteAverage, movie.voteAverage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(posterPath, overview, releaseDate, id, originalTitle, title, popularity,
                voteCount, video, voteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
