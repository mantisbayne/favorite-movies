package com.example.mbayne.favoritemovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model for list of trailers returned
 */

public class TrailerList implements Parcelable {
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers;

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.trailers);
    }

    protected TrailerList(Parcel in) {
        this.trailers = in.createTypedArrayList(Trailer.CREATOR);
    }

    public static final Creator<TrailerList> CREATOR = new Creator<TrailerList>() {
        @Override
        public TrailerList createFromParcel(Parcel source) {
            return new TrailerList(source);
        }

        @Override
        public TrailerList[] newArray(int size) {
            return new TrailerList[size];
        }
    };

    public List<Trailer> getTrailers() {
        return trailers;
    }
}
