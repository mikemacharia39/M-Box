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

package com.derrick.Utils;

import android.content.Context;

import com.derrick.movies.AboutUsActivity;
import com.derrick.movies.R;

/**
 * Created by derrick.njeru on 1/21/2017.
 */

public class StringManager {

    public static int getTitleResId(AboutUsActivity.AboutItem item) {
        switch (item) {
            case OPEN_SOURCE:
                return R.string.about_open_source_title;
            case POWERED_BY_TMDB:
                return R.string.about_powered_tmdb_title;
        }
        return 0;
    }

    public static String getSubtitle(Context context, AboutUsActivity.AboutItem item) {
        switch (item) {
            case OPEN_SOURCE:
                return context.getString(R.string.about_open_source_content);
            case POWERED_BY_TMDB:
                return context.getString(R.string.about_powered_tmdb_content);
        }
        return null;
    }
}
