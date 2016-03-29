package com.pescod.loginysu.activity;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.pescod.loginysu.utils.ExcelOperation;
import com.pescod.loginysu.utils.HttpCallbackListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/24.
 */
public class AccountManageActivity extends BaseActivity {

    private ListView listView;
    private CheckBox checkBox;
    private List<AccountInfo> listAccount;
    private AccountManageDB accountManageDB;
    private AccountManageDB manageDB;
    private ExcelOperation excelOperation;
    private ProgressDialog progressDialog;

    private boolean isEdit = false;//是否已经点击编辑按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_manage);

        checkBox = (CheckBox)findViewById(R.id.checkbox);

        manageDB = AccountManageDB.getInstance(AccountManageActivity.this);
        accountManageDB = AccountManageDB.getInstance(AccountManageActivity.this);
        //excelOperation = ExcelOperation.getInstance();

        listAccount = accountManageDB.loadAccountInfo();
        AccountAdapter accountAdapter = new AccountAdapter(
                AccountManageActivity.this,R.layout.account_list,listAccount);
        listView = (ListView)findViewById(R.id.account_list);
        listView.setAdapter(accountAdapter);

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
                AccountManageActivity.this.setResult(0, intent);
                AccountManageActivity.this.finish();
//                startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
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
                    showProgressDialog("正在读取Excel文件...");
                    ExcelOperation.readExcel("/sdcard/Documents/T_4G.xls", new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            closeProgressDialog();
                            List<AccountInfo> list = ExcelOperation.accountInfoList;
                            for (AccountInfo accountInfo : list) {
                                manageDB.saveAccount(accountInfo);
                            }
                            listAccount.clear();
                            listAccount = accountManageDB.loadAccountInfo();
                            AccountAdapter accountAdapter = new AccountAdapter(
                                    AccountManageActivity.this,R.layout.account_list,listAccount);
                            listView = (ListView)findViewById(R.id.account_list);
                            listView.setAdapter(accountAdapter);
                        }

                        @Override
                        public void onError(String e) {
                            Log.d("AccountManageActivity", e.toString());
                        }
                    });

                }
            }
        });
        builder.show();
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

    public void edit_onClick(View view){
        if (!isEdit){
            for(int i = 0; i < listView.getChildCount(); i++){
                View view1 = listView.getChildAt(i);
                CheckBox cb = (CheckBox)view1.findViewById(R.id.checkbox);
                cb.setCursorVisible(true);
            }
        }else{
            for(int i = 0; i < listView.getChildCount(); i++){
                View view1 = listView.getChildAt(i);
                CheckBox cb = (CheckBox)view1.findViewById(R.id.checkbox);
                cb.setCursorVisible(false);
            }
        }
    }

    public void test_all_account_onClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒");
        builder.setMessage("测试帐号可用性，可能花费很长时间，确定测试？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String account;
                String password;
                final String MKKey = "";
                final String address = "http://202.206.240.243/";
                Map<String,String> params = new HashMap<String, String>();
                showProgressDialog("正在测试，请稍侯...");

            }
        });
        builder.show();
    }
}
