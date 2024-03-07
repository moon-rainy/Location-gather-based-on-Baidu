package com.example.maplocationdemo.DataBase.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.maplocationdemo.DataBase.Address;

public class DBConnectionTask extends AsyncTask<Void, Void, Void> {
    private Address address;
    private final Context context;

    public DBConnectionTask(Context context, Address address) {
       this.context = context;
       this.address = address;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        CloudBDConnection.link(address);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // 数据插入成功后显示提示消息
        Toast.makeText(context, "数据插入成功", Toast.LENGTH_SHORT).show();
    }

}
