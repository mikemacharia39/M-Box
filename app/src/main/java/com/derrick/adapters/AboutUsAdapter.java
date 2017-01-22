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
import com.derrick.Utils.StringManager;
import com.derrick.models.Movie;
import com.derrick.movies.AboutUsActivity;
import com.derrick.movies.R;

import java.util.ArrayList;
import java.util.List;

import static com.derrick.Utils.ApiKey.IMAGE_BASE;

/**
 * Created by derrick.njeru on 1/22/2017.
 */

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.AboutUsViewHolder> {
    private List<AboutUsActivity.AboutItem> items;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public AboutUsAdapter(Context context, List<AboutUsActivity.AboutItem> items) {
        this.layoutInflater = layoutInflater.from(context);
        this.mContext = context;
        this.items = items;
    }


    @Override
    public AboutUsAdapter.AboutUsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.about_row, parent, false);
        AboutUsAdapter.AboutUsViewHolder viewHolder = new AboutUsAdapter.AboutUsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AboutUsAdapter.AboutUsViewHolder holder, int position) {
        AboutUsActivity.AboutItem item = items.get(position);
        holder.txt_title.setText(StringManager.getTitleResId(item));
        holder.txt_sub_title.setText(StringManager.getSubtitle(mContext, item));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class AboutUsViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_title, txt_sub_title;


        public AboutUsViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.title);
            txt_sub_title = (TextView) itemView.findViewById(R.id.sub_title);
        }
    }
}
