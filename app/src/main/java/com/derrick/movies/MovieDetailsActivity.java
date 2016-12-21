/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.derrick.movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.derrick.Utils.ApiKey;
import com.derrick.models.Movie;
import com.derrick.models.MoviesDetails;
import com.derrick.models.Result;
import com.derrick.models.Videos;
import com.derrick.rest.ApiClient;
import com.derrick.rest.ApiInterface;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.derrick.Utils.ApiKey.IMAGE_BASE;
import static com.derrick.Utils.ConstantsValues.IMAGE_YOUTUBE;
import static com.derrick.Utils.ConstantsValues.YOUTUBE_QUALITY;

public class MovieDetailsActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private static final String TAG = MainActivity.class.getSimpleName();
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    public static final String EXTRA_IMAGE = "DetailActivity:image";
    public static final String EXTRA_BACKDROP = "DetailActivity:backdrop";
    public static final String EXTRA_TITLE = "DetailActivity:title";
    public static final String EXTRA_ID = "DetailActivity:id";
    public static final String EXTRA_OVERVIEW = "DetailActivity:overview";
    private String poster, title, backdrop, tag_line, overview;
    private int movie_id;
    @BindView(R.id.flexible_collapsing)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.backdrop)
    ImageView imageBackdrop;
    Toolbar toolbar;
    @BindView(R.id.tagline_layout)
    RelativeLayout TagLineLayout;
    @BindView(R.id.tagline)
    TextView txt_tagline;
    @BindView(R.id.expandable_text)
    TextView txt_synopsis;
    ExpandableTextView txt_exp_synopsis;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.card_header_cast)
    FrameLayout layoutMore;
    List<Result> videosList = new ArrayList<>();

    //trailers
    @BindView(R.id.card_content)
    LinearLayout trailerSlider;

    @BindView(R.id.trailerLayout)
    FrameLayout trailerLayout;


    @BindView(R.id.txt_title_trailer)
    TextView txt_title_trailer;

    Typeface robotoCondensed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.flexible_example_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.flexible_example_appbar);
        appbar.addOnOffsetChangedListener(this);

        ImageView image = (ImageView) findViewById(R.id.thumbnail);
        txt_exp_synopsis = (ExpandableTextView) findViewById(R.id.expand_text_view);

        ViewCompat.setTransitionName(image, EXTRA_IMAGE);
        ViewCompat.setTransitionName(imageBackdrop, EXTRA_IMAGE);
        poster = getIntent().getStringExtra(EXTRA_IMAGE);
        title = getIntent().getStringExtra(EXTRA_TITLE);
        backdrop = getIntent().getStringExtra(EXTRA_BACKDROP);
        movie_id = getIntent().getIntExtra(EXTRA_ID, -1);
        overview = getIntent().getStringExtra(EXTRA_OVERVIEW);


        toolbarLayout.setTitle(title);
        loadPoster(getBaseContext(), poster, image);
        loadBackDrop(getBaseContext(), backdrop, imageBackdrop);

        //set font family of synopsis and tagline
        robotoCondensed = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab-Regular.ttf");
        txt_synopsis.setTypeface(robotoCondensed);
        txt_tagline.setTypeface(robotoCondensed);

        layoutMore.setOnClickListener(this);
        trailerSlider.setOnClickListener(this);

        getMovieDetails(movie_id);


    }

    private void getMovieDetails(int movie_id) {
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiService =
                ApiClient.getClient(getBaseContext()).create(ApiInterface.class);

        if (movie_id != -1) {
            Call<MoviesDetails> call = apiService.getMovieDetails(movie_id);
            call.enqueue(new Callback<MoviesDetails>() {
                @Override
                public void onResponse(Call<MoviesDetails> call, Response<MoviesDetails> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response != null) {
                        setMovieBasicDetails(response.body());
                        Videos videos = response.body().getVideos();
                        setTrailers(videos);


                    }


                }

                @Override
                public void onFailure(Call<MoviesDetails> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "no id found", Toast.LENGTH_SHORT).show();
        }

    }

    private void setTrailers(Videos videos) {
        videosList = videos.getResults();
        int size = videosList.size();
        if (size == 1) {
            txt_title_trailer.setText("Trailer");
        }


        for (int i = 0; i < size; i++) {

            Result result = videosList.get(i);
            String url = IMAGE_YOUTUBE + result.getKey() + YOUTUBE_QUALITY;
            ImageView imageView;
            ImageView imageView_play;
            TextView txt_name;

            ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(70, 70);
            ViewGroup.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            RelativeLayout relativeLayout = new RelativeLayout(this);
            relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));


            if (size == 1) {
                imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } else {
                imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(500, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            imageView_play = new ImageView(getApplicationContext());
            imageView_play.setLayoutParams(lp);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) imageView_play.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            imageView_play.setLayoutParams(layoutParams);
            imageView_play.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Drawable drawable = getResources().getDrawable(getResources()
                    .getIdentifier("play", "drawable", getPackageName()));
            imageView_play.setImageDrawable(drawable);

            txt_name = new TextView(getApplicationContext());
            txt_name.setLayoutParams(lp2);
            RelativeLayout.LayoutParams layoutParams2 =
                    (RelativeLayout.LayoutParams) txt_name.getLayoutParams();
            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams2.setMargins(8, 8, 8, 16);
            txt_name.setLayoutParams(layoutParams2);
            txt_name.setTypeface(robotoCondensed);
            txt_name.setText(result.getName());
            txt_name.setTextColor(getResources().getColor(R.color.colorWhite));


            Glide.with(getBaseContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            return false;
                        }
                    })
                    .into(imageView);

            // trailerSlider.addView(imageView_play);
            relativeLayout.addView(imageView);
            relativeLayout.addView(imageView_play);
            relativeLayout.addView(txt_name);
            trailerSlider.addView(relativeLayout);

        }

    }

    private void setMovieBasicDetails(MoviesDetails body) {
        tag_line = body.getTagline();
        txt_tagline.setText(tag_line);
        txt_exp_synopsis.setText(overview);
    }

    private void loadPoster(Context baseContext, String url, final ImageView image) {
        Glide.with(baseContext)
                .load(IMAGE_BASE + url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Bitmap bitmap = ((GlideBitmapDrawable) resource).getBitmap();
                        if (bitmap != null) {
                            Palette.from(bitmap).maximumColorCount(24).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {

                                    int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));
                                    float[] hsv = new float[3];
                                    Color.colorToHSV(vibrantColor, hsv);
                                    hsv[2] *= 0.8f;
                                    int darkerColor = Color.HSVToColor(hsv);
                                    toolbarLayout.setContentScrimColor(vibrantColor);
                                    toolbarLayout.setStatusBarScrimColor(darkerColor);
                                    setStatusBarColor(darkerColor);
                                    TagLineLayout.setBackgroundColor(vibrantColor);
                                    //imageBackdrop.setColorFilter(new ColorMatrixColorFilter(geColorMatrix()));


                                }
                            });


                        }
                        return false;
                    }
                })
                .into(image);
    }

    private ColorMatrix geColorMatrix() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(2);
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1, 1, 0.8f, 1);
        colorMatrix.postConcat(colorScale);

        return colorMatrix;

    }

    private void loadBackDrop(Context baseContext, String url, ImageView image) {
        Glide.with(baseContext)
                .load(ApiKey.IMAGE_BASE + url)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
    }


    public static void launch(FragmentActivity activity, View viewById, Movie movies) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, viewById, EXTRA_IMAGE);
        Intent intent = new Intent(activity, MovieDetailsActivity.class);

        intent.putExtra(EXTRA_IMAGE, movies.getPosterPath());
        intent.putExtra(EXTRA_BACKDROP, movies.getBackdropPath());
        intent.putExtra(EXTRA_TITLE, movies.getTitle());
        intent.putExtra(EXTRA_ID, movies.getId());
        intent.putExtra(EXTRA_OVERVIEW, movies.getOverview());
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                //  ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;

            }
        }
    }


    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        if (v == layoutMore) {
            i = new Intent(MovieDetailsActivity.this, MoreCastCrewActivity.class);
            startActivity(i);

        }
        if (v == trailerSlider) {
            i = new Intent(MovieDetailsActivity.this, MoreCastCrewActivity.class);
            startActivity(i);

        }
    }
}
