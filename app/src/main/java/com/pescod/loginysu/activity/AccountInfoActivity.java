package com.pescod.loginysu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.pescod.loginysu.R;
import com.pescod.loginysu.db.AccountManageDB;
import com.pescod.loginysu.model.AccountInfo;
import com.pescod.loginysu.utils.HttpCallbackListener;
import com.pescod.loginysu.utils.HttpUtil;
import com.pescod.loginysu.utils.Utility;


/**
 * Created by Administrator on 2015/12/26.
 */
public class AccountInfoActivity extends BaseActivity {

    TextView used_time;
    TextView used_flux;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private AccountManageDB accountManageDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_info);
        accountManageDB = AccountManageDB.getInstance(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        used_time = (TextView)findViewById(R.id.use_time_info);
        used_flux = (TextView)findViewById(R.id.use_fluxz_info);
        Intent intent = getIntent();
        getInfoFromServer();

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setStrAccount(intent.getStringExtra("account"));
        accountInfo.setStrPassword(intent.getStringExtra("password"));
        accountInfo.setIsTest(true);
        accountInfo.setIsAvailable(true);
        accountManageDB.saveAccount(accountInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void logout(View view){
        String addresss = "http://202.206.240.243/F.htm";
        HttpUtil.sendLogoutHttpRequest(addresss, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("logoutOK".equals(response)){
                    Log.d("AccountInfoActivity","onFinish has run!");
                    Intent intent = new Intent("com.pescod.loginysu.FORCE_OFFLINE");
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    public void getInfoFromServer(){
        final String address = "http://202.206.240.243/";

        getAccountInfoFromPref();
        HttpUtil.sendAccountInfoHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result;
                result = Utility.handleAccountInfoResponse(response);
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
            public void onError(String e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AccountInfoActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void getAccountInfoFromPref(){
        used_time.setText("已使用时间:" + preferences.getString("used_time", "")+"Min");
        used_flux.setText("已使用流量:" + preferences.getString("used_flux", "")+"MByte");
    }

    public void refesh_click(View view){
        getInfoFromServer();
    }
}
