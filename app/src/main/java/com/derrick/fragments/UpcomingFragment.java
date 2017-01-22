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

package com.derrick.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.derrick.Utils.ApiKey;
import com.derrick.Utils.ClickListener;
import com.derrick.Utils.EndlessScrollRecyclListenerGrid;
import com.derrick.Utils.MarginDecoration;
import com.derrick.Utils.RecyclerViewListener;
import com.derrick.adapters.MovieAdapter;
import com.derrick.models.Movie;
import com.derrick.models.MovieResponse;
import com.derrick.movies.MovieDetailsActivity;
import com.derrick.movies.MainActivity;
import com.derrick.movies.R;
import com.derrick.rest.ApiClient;
import com.derrick.rest.ApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    RecyclerView mRecyclerView;
    ArrayList<Movie> movies = new ArrayList<>();
    ArrayList<Movie> moviesMore = new ArrayList<>();
    private static final String STATE_MOVIES = "state_movies";
    @BindView(R.id.error_view)
    ErrorView mErrorView;
    private MovieAdapter movieAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();


    public UpcomingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingFragment newInstance(String param1, String param2) {
        UpcomingFragment fragment = new UpcomingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, movies);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, v);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new MarginDecoration(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(getActivity());

        if (savedInstanceState != null) {
            progressBar.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);
            movies = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            movieAdapter.setMovieList(movies);

        } else {
            getMoviesDetails(ApiKey.API_KEY, 1);

        }

        mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                progressBar.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                getMoviesDetails(ApiKey.API_KEY, 1);

            }
        });
        mRecyclerView.addOnScrollListener(new EndlessScrollRecyclListenerGrid() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                getMoviesDetailsMore(ApiKey.API_KEY, page);
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerViewListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                Movie movie = movies.get(position);
                MovieDetailsActivity.launch(getActivity(), view.findViewById(R.id.thumbnail),movie);
                //Intent i=new Intent(getActivity(), MovieDetailsActivity.class);
                // startActivity(i);
                // MovieDetailsActivity.start(getActivity());

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return v;
    }

    private void getMoviesDetails(String ApiKey, int page) {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService =
                ApiClient.getClient(getActivity()).create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getUpcomingMoviesDetails(ApiKey, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                progressBar.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    movies = response.body().getResults();
                    movieAdapter.setMovieList(movies);
                    mRecyclerView.setAdapter(movieAdapter);
                } else {
                    Toast.makeText(getActivity(), "header" + response.headers() + "code" + response.code() + "errorbody" + response.errorBody() + "errorMessage" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                // Log error here since request failed
                Log.e(TAG, t.toString());
                mErrorView.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getMoviesDetailsMore(String ApiKey, int page) {
        ApiInterface apiService =
                ApiClient.getClient(getActivity()).create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getUpcomingMoviesDetails(ApiKey, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                moviesMore = response.body().getResults();
                int curSize = movieAdapter.getItemCount();
                movies.addAll(moviesMore);
                movieAdapter.notifyItemRangeInserted(curSize, movies.size() - 1);

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());


            }
        });
    }


}
