package com.pescod.loginysu.utils;

/**
 * Created by Administrator on 1/1/2016.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
