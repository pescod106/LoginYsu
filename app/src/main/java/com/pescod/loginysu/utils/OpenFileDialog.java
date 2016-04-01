package com.pescod.loginysu.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.pescod.loginysu.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by pescod on 3/29/2016.
 */
public class OpenFileDialog  {
    public static String tag = "OpenFileDialog";
    static final public String sRoot = "/";
    static final public String sParent = "..";
    static final public String sFolder = ".";
    static final public String sEmpty = "";
    static final public String sOnErrorMsg = "No rights to access";

    /**
     *
     * @param id 对话框Id
     * @param context 上下文
     * @param title 对话框标题
     * @param callbackBundle 一个传递Bundle参数的回调接口
     * @param suffix 需要选择的文件后缀，
     * @param images 一年过来根据后缀显示的图标资源ID
     * @return
     */
    public static Dialog createDialog(int id, Context context, String title,
                                      CallbackBundle callbackBundle, String suffix,
                                      Map<String,Integer> images){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(new FileSelectView(context,id,callbackBundle,suffix,images));
        Dialog dialog = builder.create();
        dialog.setTitle(title);
        return dialog;
    }

    static class FileSelectView extends ListView implements AdapterView.OnItemClickListener{
        private CallbackBundle callbackBundle = null;
        private String path = sRoot;
        private List<Map<String,Object>> list = null;
        private int dialogID = 0;

        private String suffix = null;
        private Map<String,Integer> imageMap = null;

        public FileSelectView(Context context,int dialogId,CallbackBundle callbackBundle,
                              String suffix,Map<String,Integer> images){
            super(context);
            this.imageMap = images;
            this.suffix = suffix==null?"":suffix.toLowerCase();
            this.callbackBundle = callbackBundle;
            this.dialogID = dialogId;
            this.setOnItemClickListener(this);
            refreshFileList();
        }

        /**
         * 获得文件的后缀名
         * @param fileName 文件名
         * @return
         */
        private String getSuffix(String fileName){
            int dix = fileName.lastIndexOf('.');
            if (dix<0){
                return "";
            }else{
                return fileName.substring(dix+1);
            }
        }

        /**
         * 获取图片ID
         * @param s
         * @return
         */
        private int getImageId(String s){
            if (imageMap==null){
                return 0;
            }else if (imageMap.containsKey(s)){
                return imageMap.get(s);
            }else if (imageMap.containsKey(sEmpty)){
                return imageMap.get(sEmpty);
            }else{
                return 0;
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String pt = (String)list.get(position).get("path");
            String fn = (String)list.get(position).get("name");
            if (fn.equals(sRoot)||fn.equals(sParent)){
                File fl = new File(pt);
                String ppt = fl.getParent();
                if (ppt!=null){
                    path = ppt;
                }else{
                    path = sRoot;
                }
            }else{
                File fl = new File(pt);
                if (fl.isFile()){
                    ((Activity)getContext()).dismissDialog(this.dialogID);

                    Bundle bundle = new Bundle();
                    bundle.putString("path",pt);
                    bundle.putString("name",fn);
                    this.callbackBundle.callback(bundle);
                    return;
                }else if (fl.isDirectory()){
                    path = pt;
                }
            }
            this.refreshFileList();
        }

        private int refreshFileList(){
            File[] files = null;
            try{
                files = new File(path).listFiles();
            }catch(Exception e){
                files = null;
                e.printStackTrace();
            }
            if (files==null){
                Toast.makeText(getContext(),sOnErrorMsg, Toast.LENGTH_SHORT).show();
                return -1;
            }
            if (list!=null){
                list.clear();
            }else{
                list = new ArrayList<Map<String,Object>>(files.length);
            }
            //用来先保存文件夹和文件的两个列表
            ArrayList<Map<String,Object>> lFolders = new ArrayList<Map<String, Object>>();
            ArrayList<Map<String,Object>> lFiles = new ArrayList<Map<String, Object>>();

            if (!this.path.equals(sRoot)){
                //添加根目录和上一层目录
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("name",sRoot);
                map.put("path",sRoot);
                map.put("img",getImageId(sRoot));
                list.add(map);

                map = new HashMap<String,Object>();
                map.put("name",sParent);
                map.put("path",path);
                map.put("img",getImageId(sParent));
                list.add(map);
            }

            for (File file:files){
                if (file.isDirectory()&&file.listFiles()!=null){
                    //添加文件夹
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("name",file.getName());
                    map.put("path",file.getPath());
                    map.put("img",getImageId(sFolder));
                    lFolders.add(map);
                }else if (file.isFile()){
                    String sf = getSuffix(file.getName()).toLowerCase();
                    if (suffix == null || suffix.length() == 0 || (sf.length() > 0 && suffix.indexOf("." + sf + ";") >= 0)) {
                        Map<String,Object> map = new HashMap<String,Object>();
                        map.put("name",file.getName());
                        map.put("path",file.getPath());
                        map.put("img",getImageId(sf));
                        lFiles.add(map);
                    }
                }
            }

            list.addAll(lFolders);
            list.addAll(lFiles);

            SimpleAdapter adapter = new SimpleAdapter(getContext(),list,
                    R.layout.filedialogitem,new String[]{"img","name","path"},
                    new int[]{R.id.filedialogitem_img,R.id.filedialogitem_name,
                    R.id.filedialogitem_path});
            this.setAdapter(adapter);

            return files.length;
        }
    }
}
