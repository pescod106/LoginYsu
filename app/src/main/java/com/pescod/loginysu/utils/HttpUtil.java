package com.pescod.loginysu.utils;

import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 1/1/2016.
 */
public class HttpUtil{
    public static void sendLoginHttpRequest(final Map<String,String> params,final String encode,final String address,
                                            final HttpCallbackListener listener){

        if (!params.isEmpty()){
            final byte[] data = getRequestData(params,encode).toString().getBytes();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    try{
                        URL url = new URL(address);
                        connection = (HttpURLConnection)url.openConnection();
                        /**
                         * 设置是否向httpUrlConnection输出，因为这个是post请求，
                         * 参数要放在http正文内，因此需要设为true, 默认情况下是false;
                         */
                        connection.setDoOutput(true);
                        // 设置是否从httpUrlConnection读入，默认情况下是true;
                        connection.setDoInput(true);
                        // Post 请求不能使用缓存
                        //connection.setUseCaches(false);
                        connection.setRequestProperty("Connection", "keep-alive");
                        connection.setRequestProperty("Host", "202.206.240.243");
                        connection.setRequestProperty("Referer", "http://202.206.240.243/");
                        connection.setRequestProperty("Content-Length","39");
                        connection.setRequestProperty("Accept-Encoding","gzip, deflate");
                        connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                        connection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);

                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(data);

                        int response = connection.getResponseCode();
                        if (response==200){
                            Log.d("httpUtul","请求成功");
                            if (listener!=null){
                                listener.onFinish("OK");
                            }
                        }else {
                            Log.d("httpUtul","请求error");
                        }

                    }catch (Exception e){
                        if(listener!=null){
                            listener.onError(e);
                        }
                    }finally {
                        if(connection!=null){
                            connection.disconnect();
                        }
                    }
                }
            }).start();
        }

    }

    public static StringBuffer getRequestData(Map<String,String> params,String encode){
        StringBuffer stringBuffer = new StringBuffer();
        try{
            for (Map.Entry<String,String> entry:params.entrySet()){
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(),encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public static void sendAccountInfoHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    if (listener!=null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if(listener!=null){
                        listener.onError(e);
                    }
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }
}
