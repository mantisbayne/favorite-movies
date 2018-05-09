package com.example.mbayne.favoritemovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for Movies database
 */

public class MovieContract {

    // Authority should be the same as in teh android manifest
    public static final String AUTHORITY ="com.meredithbayne.toppopularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    //MovieEntry is an inner class that defines the contens of the Movie table
    public static final class MovieEntry implements BaseColumns {

        // URI
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        // Movie table name
        public static final String TABLE_NAME = "movies";

        // Columns
        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
    }
}