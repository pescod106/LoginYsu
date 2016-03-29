package com.pescod.loginysu.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import org.jsoup.helper.HttpConnection;

import java.io.BufferedReader;
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
                        connection.setRequestMethod("POST");
                        /**
                         * 设置是否向httpUrlConnection输出，因为这个是post请求，
                         * 参数要放在http正文内，因此需要设为true, 默认情况下是false;
                         */
                        connection.setDoOutput(true);
                        // 设置是否从httpUrlConnection读入，默认情况下是true;
//                        connection.setDoInput(true);
                        // Post 请求不能使用缓存
                        connection.setUseCaches(false);
//                        connection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//                        connection.setRequestProperty("Accept-Encoding","gzip, deflate");
//                        connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
//                        connection.setRequestProperty("Cache-Control","max-age=0");
//                        connection.setRequestProperty("Connection", "keep-alive");
//                        connection.setRequestProperty("Content-Length","39");
//                        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                        connection.setRequestProperty("Host", "202.206.240.243");
//                        connection.setRequestProperty("Origin","http://202.206.240.243");
                        connection.setRequestProperty("Referer", "http://202.206.240.243/");
//                        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
//                        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) " +
//                                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.108 Safari/537.36");

                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(data);

                        int response = connection.getResponseCode();
                        if (response==200){
                            InputStream in = connection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"gb2312"));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine())!=null){
                                stringBuilder.append(line);
                            }
                            String result = stringBuilder.toString();
                            if (result.contains("您已经成功登录。")&&listener!=null){
                                listener.onFinish("OK");
                                Log.d("httpUtul","-----请求成功-----");
                            }else{
                                listener.onError("");
                            }

                        }else {
                            Log.d("httpUtul","请求error");
                        }

                    }catch (Exception e){
                        if(listener!=null){
                            listener.onError(e.toString());
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

    public static void sendLoginHttpIsSuccessed(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "text/html,application/xhtml+xml," +
                            "application/xml;q=0.9,image/webp,*/*;q=0.8");
                    connection.setRequestProperty("Accept-Encoding","gzip, deflate, sdch");
                    connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
                    connection.setRequestProperty("Cache-Control","max-age=0");
                    connection.setRequestProperty("Connection","keep-alive");
                    connection.setRequestProperty("Host","202.206.240.243:9002");
                    connection.setRequestProperty("Upgrade-Insecure-Requests","1");
                    connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
                    connection.setDoInput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    int result = connection.getResponseCode();
                    if ((result==200)){
                        if (listener!=null){
                            listener.onFinish("login successed!");
                        }
                    }else{
                        Toast.makeText(MyApplication.getContext(),"request error",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    if (listener!=null){
                        listener.onError(e.toString());
                    }
                }finally{
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
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
//                    connection.setRequestProperty("Content-Type", "text/html; charset=gb2312");
//                    connection.setRequestProperty("Host", "202.206.240.243");
//                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//                    connection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//                    connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//                    connection.setRequestProperty("Accept-Encoding","gzip, deflate");
//                    connection.setRequestProperty("Connection","keep-alive");
//                    connection.setRequestProperty("Cache-Control", "max-age=0");
                    connection.setDoInput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    //Parser parser = new Parser(connection);
                    int result = connection.getResponseCode();
                    if ((result==200)){
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"gb2312"));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine())!=null){
                            response.append(line);
                        }
                        if (listener!=null){
                            listener.onFinish(response.toString());
                        }
                    }else{
                        Toast.makeText(MyApplication.getContext(),"request error",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    if(listener!=null){
                        listener.onError(e.toString());
                    }
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }

    public static void sendLogoutHttpRequest(final String address,final HttpCallbackListener listener){
           if(!TextUtils.isEmpty(address)){
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       HttpURLConnection connection = null;
                       try{
                           URL url = new URL(address);
                           connection = (HttpURLConnection)url.openConnection();
                           connection.setRequestMethod("GET");
                           connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                           connection.setRequestProperty("Accept-Encoding","gzip, deflate, sdch");
                           connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
                           connection.setRequestProperty("Connection","keep-alive");
                           connection.setRequestProperty("Host","202.206.240.243");
                           connection.setRequestProperty("Referer","http://202.206.240.243/");
                           connection.setRequestProperty("Upgrade-Insecure-Requests","1");
                           connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
                           connection.setReadTimeout(3000);
                           int response = connection.getResponseCode();
                           Log.d("HttpUtil","connection.getResponseCode() "+response);
                           if ((response==200)){
                               if (listener!=null){
                                   Log.d("HttpUtil","logout success!");
                                   listener.onFinish("logoutOK");
                               }else {
                                   Log.d("HttpUtil","listener is null");
                               }
                           }else{
                               Log.d("logout","error");
                           }
                       }catch (Exception e){
                           listener.onError(e.toString());
                       }finally {
                           connection.disconnect();
                       }
                   }
               }).start();
           }
    }
}
