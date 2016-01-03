package com.pescod.loginysu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.pescod.loginysu.R;
import com.pescod.loginysu.utils.HttpCallbackListener;
import com.pescod.loginysu.utils.HttpUtil;
import com.pescod.loginysu.utils.MyApplication;
import com.pescod.loginysu.utils.Utility;

/**
 * Created by Administrator on 2015/12/26.
 */
public class AccountInfoActivity extends BaseActivity {

    TextView used_time;
    TextView used_flux;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_info);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        used_time = (TextView)findViewById(R.id.use_time_info);
        used_flux = (TextView)findViewById(R.id.use_fluxz_info);
        getInfoFromServer();
    }

    public void logout(View view){
        Intent intent = new Intent("com.pescod.loginysu.FORCE_OFFLINE");
        sendBroadcast(intent);
    }

    public void getInfoFromServer(){
        final String address = "http://202.206.240.243/";
        boolean result;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//               result =  Utility.handleAccountInfoResponse(address);
//            }
//        });

        getAccountInfoFromPref();
        HttpUtil.sendAccountInfoHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result;
                result =  Utility.handleAccountInfoResponse(response);
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getAccountInfoFromPref();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AccountInfoActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void getAccountInfoFromPref(){
        used_time.setText("已使用时间 Used time :"+preferences.getString("used_time",""));
        used_flux.setText("已使用流量 Used flux :"+preferences.getString("used_flux",""));
    }
}
