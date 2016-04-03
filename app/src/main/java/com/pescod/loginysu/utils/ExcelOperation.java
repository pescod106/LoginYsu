package com.pescod.loginysu.utils;

import android.util.Log;

import com.pescod.loginysu.model.AccountInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by pescod on 3/18/2016.
 */
public class ExcelOperation {
    public static List<AccountInfo> accountInfoList;
//    public synchronized static ExcelOperation getInstance(){
//        if (excelOperation==null){
//            excelOperation = new ExcelOperation();
//        }
//        return excelOperation;
//    }
    /**
     * 读取Excel文件
     * @param path Excel文件路径
     * @return 将从表格里面读取的所有信息保存到List中
     */
    public static void readExcel(final String path,final HttpCallbackListener listener){
        accountInfoList = new ArrayList<AccountInfo>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //Log.d("ExcelOperation","------------error-------------");
                    InputStream inputStream = new FileInputStream(path);
                    Workbook workbook = Workbook.getWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet(0);
                    int rows = sheet.getRows();
                    for (int i=1;i<rows;i++){
                        AccountInfo accountInfo = new AccountInfo();
                        accountInfo.setStrAccount(sheet.getCell(1,i).getContents());
                        accountInfo.setStrPassword(sheet.getCell(0,i).getContents());
                        accountInfo.setIsAvailable(false);
                        accountInfo.setIsTest(false);
                        accountInfoList.add(accountInfo);
                    }
                    workbook.close();
                    if (listener!=null){
                        listener.onFinish("read Excel successed!");
                    }
                }catch (Exception e){
                    if (listener!=null){
                        listener.onFinish(e.toString());
                    }
                }
            }
        }).start();
        //return accountInfoList;
    }

    public static void writeExcel(List<AccountInfo> accountInfoArrayList,
                            String path){
        try{
            WritableWorkbook book = Workbook.createWorkbook(new
                    File(path));
            WritableSheet writableSheet = book.createSheet("Sheet1",1);
            for (int i=0;i<accountInfoArrayList.size();i++){
                Label label1 = new Label(0,i,accountInfoArrayList.get(i).getStrAccount());
                Label label2 = new Label(1,i,accountInfoArrayList.get(i).getStrPassword());
                writableSheet.addCell(label1);
                writableSheet.addCell(label2);
            }
            book.write();
            book.close();
        }catch(Exception e){
            e.printStackTrace();
            Log.d("ExcelOperation",e.toString());
        }
    }
}
