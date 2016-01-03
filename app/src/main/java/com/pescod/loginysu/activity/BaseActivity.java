package com.pescod.loginysu.activity;

import android.app.Activity;
import android.os.Bundle;

import com.pescod.loginysu.utils.ActivityCollector;

/**
 * Created by Administrator on 1/1/2016.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
