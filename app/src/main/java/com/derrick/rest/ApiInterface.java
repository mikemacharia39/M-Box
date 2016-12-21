package com.derrick.rest;

import com.derrick.models.MovieResponse;
import com.derrick.models.MoviesDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by DERRICK on 7/7/2016.
 */


public interface ApiInterface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMoviesDetailsNoPagination(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMoviesDetails(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMoviesDetails(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMoviesDetails(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}?api_key=bb7ce524c99f7a42c1f154f0a7b82d50&append_to_response=videos,credits,crew,similar")
    Call<MoviesDetails> getMovieDetails(@Path("id") int id);

}