package com.example.maplocationdemo.DataBase.Local;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileTransfer {
    private Context context;
    public FileTransfer(Context context) {
        this.context=context;
    }
    public void exportFile(String filename,String str){
        File files = context.getExternalFilesDir("");
        if(files==null){
            Log.e("error","Android/data/sonydafa/files获取失败");
            return;
        }
        String path=files.getAbsolutePath()+"/"+filename;
        File file = new File(path);
        Log.i("info","导出的文件目录为:"+path);
        try(FileWriter fileWriter = new FileWriter(file, true)){
            fileWriter.append(str);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
