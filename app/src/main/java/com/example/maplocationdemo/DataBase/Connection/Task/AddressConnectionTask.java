package com.example.maplocationdemo.DataBase.Connection.Task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.maplocationdemo.DataBase.Address;
import com.example.maplocationdemo.DataBase.Connection.CloudBDConnection;
import com.example.maplocationdemo.DataBase.PoiInfo;

public class AddressConnectionTask extends AsyncTask<Void, Void, Void> {
    private Address address;

    @SuppressLint("StaticFieldLeak")
    private Context context;


    public AddressConnectionTask() {
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        CloudBDConnection.linkAddress(address);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // 数据插入成功后显示提示消息
        Toast.makeText(context, "数据插入成功", Toast.LENGTH_SHORT).show();
    }

}
