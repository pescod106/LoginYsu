package com.pescod.loginysu.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pescod.loginysu.R;
import com.pescod.loginysu.model.AccountInfo;

import java.util.List;

/**
 * Created by pescod on 3/22/2016.
 */
public class AccountAdapter extends ArrayAdapter<AccountInfo> {
    private int textViewResourceId;

    public AccountAdapter(Context context,int textViewResourceId,
                          List<AccountInfo> objects){
        super(context,textViewResourceId,objects);
        this.textViewResourceId = textViewResourceId;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountInfo accountInfo = getItem(position);
        View view;
        viewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(textViewResourceId, null);
            viewHolder = new viewHolder();
            viewHolder.checkBox = (CheckBox)view.findViewById(R.id.checkbox);
            viewHolder.account_textView = (TextView)view.findViewById(R.id.account);
            viewHolder.password_textView = (TextView)view.findViewById(R.id.password);
            viewHolder.isTest_textView = (TextView)view.findViewById(R.id.isTest);
            viewHolder.isAvailable_textView = (TextView)view.findViewById(R.id.isAvailable);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (viewHolder)view.getTag();
        }
        viewHolder.checkBox.setChecked(false);
        viewHolder.account_textView.setText(accountInfo.getStrAccount());
        viewHolder.password_textView.setText(accountInfo.getStrPassword());
        viewHolder.isTest_textView.setText(accountInfo.isTest()?"1":"0");
        viewHolder.isAvailable_textView.setText(accountInfo.isAvailable()?"1":"0");
        return view;
    }

    class viewHolder{
        CheckBox checkBox;
        TextView account_textView;
        TextView password_textView;
        TextView isTest_textView;
        TextView isAvailable_textView;
    }
}
