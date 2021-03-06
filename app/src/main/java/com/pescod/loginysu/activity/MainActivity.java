package com.pescod.loginysu.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pescod.loginysu.R;
import com.pescod.loginysu.utils.HttpCallbackListener;
import com.pescod.loginysu.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private TextView changeAccount;
    private Button loginBtn;
    private CheckBox rememberPass;
    private UIHandler uiHandler;

    boolean isAccount = false;//判断帐号输入是否合法
    boolean isPassword = false;//判断密码长度是否不为零

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity","-------onActivityResult--------");
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){
            Bundle options = data.getExtras();
            accountEdit.setText(options.getString("account",""));
            passwordEdit.setText(options.getString("password", ""));
            Log.d("MainActivity","-------onActivityResult--------");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText)findViewById(R.id.account_edit);
        passwordEdit = (EditText)findViewById(R.id.password_edit);
        loginBtn = (Button)findViewById(R.id.btn_login);
        changeAccount = (TextView)findViewById(R.id.change);
        rememberPass = (CheckBox)findViewById(R.id.check_rember);
        uiHandler = new UIHandler();

        accountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (accountEdit.length()==12){
                    isAccount=true;
                    if(isAccount&&isPassword) {
                        loginBtn.setEnabled(true);
                    }
                }else if (accountEdit.length()==0){
                    passwordEdit.setText("");
                }else{
                    isAccount = false;
                    loginBtn.setEnabled(false);
                }
            }
        });
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (passwordEdit.length()!=0){
                    isPassword=true;
                    if(isAccount&&isPassword) {
                        loginBtn.setEnabled(true);
                    }
                }else{
                    isPassword = false;
                    loginBtn.setEnabled(false);
                }
            }
        });

        boolean isRemember = preferences.getBoolean("remember_password", false);
        String account = preferences.getString("account","");
        String password = preferences.getString("password","");
        Log.d("MainActivity","-------从preferences里面读取--------");
        accountEdit.setText(account);
        passwordEdit.setText(password);
        if (isRemember){
            rememberPass.setChecked(true);
            loginBtn.setEnabled(true);
        }

        changeAccount.setClickable(true);
        changeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AccountManageActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    public void btn_login(View view){
        final String account = accountEdit.getText().toString().trim();
        final String password = passwordEdit.getText().toString();
        final String address = "http://202.206.240.243/";

        Map<String,String> params = new HashMap<String,String>();
        params.put("DDDDD",account);
        params.put("upass", password);
        params.put("0MKKey", "");
        showProgressDialog();
        HttpUtil.sendLoginHttpRequest(params, "utf-8", address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                closeProgressDialog();
                if (!TextUtils.isEmpty(response)) {
                    if (rememberPass.isChecked()) {
                        writeAccInfoToPref(true, account, password);
                    } else {
                        writeAccInfoToPref(false, account, "");
                    }
                    Intent intent = new Intent(MainActivity.this, AccountInfoActivity.class);
                    intent.putExtra("account", account);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(String e) {
                closeProgressDialog();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("toast","账号或密码错误!");
                message.setData(bundle);
                MainActivity.this.uiHandler.sendMessage(message);
                //Toast.makeText(MainActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "ldap auth error");
            }
        });
    }

    /**
     * 将账号使用情况写入到SharePreference
     */
    public void writeAccInfoToPref(boolean isRemember,String account,String passwd){
        editor = preferences.edit();
        editor.putBoolean("remember_password", isRemember);
        editor.putString("account", account);
        editor.putString("password", passwd);
        editor.commit();
    }

    /**
     * 显示进度条
     */
    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度条
     */
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String message = bundle.get("toast").toString();
            Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }
}
