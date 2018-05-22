package com.example.mbayne.favoritemovies.rest;

import com.example.mbayne.favoritemovies.model.MovieList;
import com.example.mbayne.favoritemovies.model.ReviewList;
import com.example.mbayne.favoritemovies.model.TrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {
    @GET("movie/top_rated")
    Call<MovieList> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieList> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{movieId}/videos")
    Call<TrailerList> getTrailers(@Path("movieId") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movieId}/reviews")
    Call<ReviewList> getReviews(@Path("movieId") int movieId, @Query("api_key") String apiKey);
}
