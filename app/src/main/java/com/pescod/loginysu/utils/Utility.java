package com.pescod.loginysu.utils;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
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
                Log.d("Utility doc ",doc.text());
                Elements infor = doc.select("script");
                Element detailInfo = infor.first();
                Log.d("Utility response",response);
                Log.d("AccountInfoActivity", "doc is " + detailInfo.toString());
                String strDetailInfo = detailInfo.toString();
                String time = strDetailInfo.substring(40,50).trim();
                String usage = strDetailInfo.substring(58,68).trim();
                list.add(time);
                int flow=0;
                try{
                    flow = Integer.parseInt(usage);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                int flow0 = flow%1024;
                int flow1 = flow-flow0;
                flow0 = flow0*1000;
                flow0 = flow0-flow0%1024;
                String flow3=".";
                if (flow0/1024<10){
                    flow3=".00";
                }else if (flow0/1024<100){
                    flow3=".0";
                }
                usage = String.valueOf(flow1/1024)+flow3+String.valueOf(flow0/1024);
                list.add(usage);
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
