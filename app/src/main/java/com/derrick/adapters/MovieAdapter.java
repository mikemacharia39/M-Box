package com.derrick.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.derrick.models.Movie;
import com.derrick.movies.R;

import java.util.ArrayList;

import static com.derrick.Utils.ApiKey.IMAGE_BASE;

/**
 * Created by DERRICK on 7/7/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Movie> listMovies;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public MovieAdapter(Context context) {
        this.layoutInflater = layoutInflater.from(context);
        this.mContext = context;
    }

    public void setMovieList(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_items, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        Movie currentMovie = listMovies.get(position);
        holder.txt_title.setText(currentMovie.getTitle());


        Glide.with(mContext)
                .load(IMAGE_BASE + currentMovie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.txt_title.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txt_title;


        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            txt_title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
