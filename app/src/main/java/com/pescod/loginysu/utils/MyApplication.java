package com.pescod.loginysu.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 1/2/2016.
 */
public class MyApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}
