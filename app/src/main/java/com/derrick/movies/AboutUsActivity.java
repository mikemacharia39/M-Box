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

package com.derrick.movies;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.derrick.Utils.ClickListener;
import com.derrick.Utils.RecyclerViewListener;
import com.derrick.adapters.AboutUsAdapter;

import java.util.Arrays;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    List<AboutItem> items;

    public static enum AboutItem {
        OPEN_SOURCE, POWERED_BY_TMDB;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initializeViews();
        clickListener();
    }

    private void clickListener() {
        mRecyclerView.addOnItemTouchListener(new RecyclerViewListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AboutItem aboutItem = items.get(position);
                if (aboutItem.equals(AboutItem.OPEN_SOURCE))
                    startActivity(new Intent(AboutUsActivity.this, LicenceActivity.class));


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void initializeViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.about_list);
        items = Arrays.asList(AboutItem.values());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new AboutUsAdapter(this, items));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
