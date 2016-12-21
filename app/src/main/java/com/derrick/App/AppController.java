package com.derrick.App;

import android.app.Application;

/**
 * Created by DERRICK on 7/23/2016.
 */
public class AppController extends Application {

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


}