package com.example.maplocationdemo.DataBase.Connection.Task;

import android.os.AsyncTask;

import com.example.maplocationdemo.DataBase.Connection.CloudBDConnection;
import com.example.maplocationdemo.DataBase.GPSInfo;
import com.example.maplocationdemo.DataBase.PoiInfo;

public class GPSInfoConnectionTask extends AsyncTask<Void, Void, Void> {
    private GPSInfo gpsInfo;


    public void setGPSInfo(GPSInfo gpsInfo) {
        this.gpsInfo = gpsInfo;
    }

    public GPSInfoConnectionTask() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        CloudBDConnection.linkGPSInfo(gpsInfo);
        return null;
    }
}
