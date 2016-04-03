package com.pescod.loginysu.activity;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.pescod.loginysu.R;
import com.pescod.loginysu.db.AccountManageDB;
import com.pescod.loginysu.model.AccountInfo;
import com.pescod.loginysu.utils.AccountAdapter;
import com.pescod.loginysu.utils.CallbackBundle;
import com.pescod.loginysu.utils.ExcelOperation;
import com.pescod.loginysu.utils.HttpCallbackListener;
import com.pescod.loginysu.utils.HttpUtil;
import com.pescod.loginysu.utils.OpenFileDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/24.
 */
public class AccountManageActivity extends BaseActivity {

    private static final int UPDATE_LISTVIEW = 1;

    private ListView listView;
    private List<AccountInfo> listAccount;
    private AccountManageDB accountManageDB;
    private ExcelOperation excelOperation;
    private ProgressDialog progressDialog;

    private int openFileDialog = 0;

    private boolean isEdit = false;//是否已经点击编辑按钮

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_LISTVIEW:
                    listAccount.clear();
                    listAccount = accountManageDB.loadAccountInfo();
                    AccountAdapter accountAdapter = new AccountAdapter(
                            AccountManageActivity.this,R.layout.account_list,listAccount);
                    listView = (ListView)findViewById(R.id.account_list);
//                    listView.removeAllViews();
                    listView.setAdapter(accountAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_manage);


        accountManageDB = AccountManageDB.getInstance(AccountManageActivity.this);
        //excelOperation = ExcelOperation.getInstance();

        listAccount = accountManageDB.loadAccountInfo();
//        if (listAccount.size()>0){
        AccountAdapter accountAdapter = new AccountAdapter(
                AccountManageActivity.this,R.layout.account_list,listAccount);
        listView = (ListView)findViewById(R.id.account_list);
        listView.setAdapter(accountAdapter);
//        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountInfo accountInfo = listAccount.get(position);
                Intent intent = new Intent(AccountManageActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("account",accountInfo.getStrAccount());
                bundle.putString("password",accountInfo.getStrPassword());
                Log.d("AccountManageActivity", accountInfo.getStrAccount() + " " +
                        accountInfo.getStrPassword());
                intent.putExtras(bundle);
//                startActivityForResult(intent, 1);
                AccountManageActivity.this.setResult(1, intent);
                AccountManageActivity.this.finish();
//
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id==openFileDialog){
            Map<String, Integer> images = new HashMap<String, Integer>();
            // 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
            images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);   // 根目录图标
            images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);    //返回上一层的图标
            images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);   //文件夹图标
            images.put("wav", R.drawable.filedialog_wavfile);   //wav文件图标
            images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_root);
            Dialog dialog = OpenFileDialog.createDialog(id, this, "打开文件", new CallbackBundle() {
                        @Override
                        public void callback(Bundle bundle) {
                            String filepath = bundle.getString("path");
                            setTitle(filepath); // 把文件路径显示在标题上
                        }
                    },
                    ".wav;",
                    images);
        }
        return null;
    }

    public void add_account_onClick(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(AccountManageActivity.this);
        builder.setTitle("请选择");
        final String[] options = {"当项添加","从Excel文件添加"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Toast.makeText(AccountManageActivity.this, "111111", Toast.LENGTH_SHORT).show();
                } else {
                    //showDialog(openFileDialog);
                    showProgressDialog("正在读取Excel文件...");
                    ExcelOperation.readExcel("/sdcard/Documents/T_4G.xls", new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
//                            closeProgressDialog();
                            Log.d("onFinish",response);
                            List<AccountInfo> list = ExcelOperation.accountInfoList;
//                            showProgressDialog("正在将账号保存到本地数据库...");
                            for (AccountInfo accountInfo : list) {
                                if (!(accountInfo.getStrAccount().contains("S")||
                                        accountInfo.getStrAccount().contains("s")))
                                    accountManageDB.saveAccount(accountInfo);
                            }
                            closeProgressDialog();
                            finish();
                        }

                        @Override
                        public void onError(String e) {
                            Log.d("AccountManageActivity", e.toString());
                        }
                    });

                }
            }
        }).show();
    }



    /**
     * 显示进度条对话框
     * @param content 对话框的内容
     */
    private void showProgressDialog(String content){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage(content);
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    public void export_onClick(View view){
        listAccount = accountManageDB.loadAccountInfo();
        ExcelOperation.writeExcel(listAccount,"/sdcard/Documents/4G.xls");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
    }

    public void test_all_account(){
        final String address = "http://202.206.240.243/";
//                showProgressDialog("正在测试，请稍侯...");
        //int i;
        Log.d("账号个数:",String.valueOf(listAccount.size()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<listAccount.size();i++){
                    final Map<String,String> params = new HashMap<String, String>();
                    String account = listAccount.get(i).getStrAccount();
                    String password = listAccount.get(i).getStrPassword();
                    params.put("DDDDD",account);
                    params.put("upass",password);
                    params.put("0MKKey", "");
                    HttpUtil.sendLoginHttpRequest(params, "utf-8", address, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Log.d("onFinish",response);
                        }

                        @Override
                        public void onError(String e) {
                            if ("".equals(e)){
                                accountManageDB.deleteAccount(params.get("DDDDD"));
                                Log.d("deleteAccount",params.get("DDDDD"));
                            }
                        }
                    });
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    public void test_all_account_onClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒");
        builder.setMessage("测试帐号可用性，可能花费很长时间，确定测试？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                final List<AccountInfo> listAccountResult = new ArrayList<AccountInfo>();
                test_all_account();
            }
        }).show();
    }
}
