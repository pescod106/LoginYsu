package com.pescod.loginysu.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 1/1/2016.
 */
public class ActivityCollector {
    public static List<Activity> activityList = new ArrayList<Activity>();

    /**
     * when new an activity,add it to the activityList
     * @param activity
     */
    public static void addActivity(Activity activity){
        if (!activityList.contains(activity)){
            activityList.add(activity);
        }
    }

    /**
     * remove the activity from activityList
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    /**
    *finalize all the activities
     */
    public static void finishAll(){
        for (Activity activity : activityList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
