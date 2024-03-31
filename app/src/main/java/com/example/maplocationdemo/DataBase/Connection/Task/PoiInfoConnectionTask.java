package com.example.maplocationdemo.DataBase.Connection.Task;

import android.os.AsyncTask;

import com.example.maplocationdemo.DataBase.Connection.CloudBDConnection;
import com.example.maplocationdemo.DataBase.PoiInfo;

public class PoiInfoConnectionTask extends AsyncTask<Void, Void, Void> {
    private PoiInfo poiInfo;


    public void setPoiInfo(PoiInfo poiInfo) {
        this.poiInfo = poiInfo;
    }

    public PoiInfoConnectionTask() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        CloudBDConnection.linkPoiInfo(poiInfo);
        return null;
    }


}
