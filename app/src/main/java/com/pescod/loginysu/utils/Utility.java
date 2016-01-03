package com.pescod.loginysu.utils;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 1/1/2016.
 */
public class Utility {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    public synchronized static boolean handleAccountInfoResponse(String response){
        if (!TextUtils.isEmpty(response)){
            List<String> list = new ArrayList<String>();
            try {
                Document doc = Jsoup.parse(response);
                Elements infor = doc.select(".f1");
                Log.d("AccountInfoActivity", "doc is " + doc.text());
                //Elements tbody = doc.select("tbody");
                Elements tr = infor.select("p");
                //Elements allInfo = infor.select("p");
                for (org.jsoup.nodes.Element element : tr){
                    if (element.text()!=null&&""!=element.text()){
                        list.add(element.text());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
            editor = preferences.edit();
            if(list.size()>0){
                editor.putString("used_time",list.get(0));
                editor.putString("used_flux",list.get(1));
                editor.commit();
            }else{
                Toast.makeText(MyApplication.getContext(),"size = 0",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
}
