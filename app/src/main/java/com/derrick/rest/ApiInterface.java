/*
 * Copyright 2016 Derrick Njeru
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.derrick.rest;

import com.derrick.Utils.ApiKey;
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

    @GET("movie/{id}?api_key=" + ApiKey.API_KEY + "&append_to_response=videos,credits,crew,similar")
    Call<MoviesDetails> getMovieDetails(@Path("id") int id);

}