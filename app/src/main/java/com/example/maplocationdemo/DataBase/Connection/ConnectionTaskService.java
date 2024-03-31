package com.example.maplocationdemo.DataBase.Connection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.example.maplocationdemo.DataBase.Address;
import com.example.maplocationdemo.DataBase.Connection.Task.AddressConnectionTask;
import com.example.maplocationdemo.DataBase.Connection.Task.GPSInfoConnectionTask;
import com.example.maplocationdemo.DataBase.Connection.Task.PoiInfoConnectionTask;
import com.example.maplocationdemo.DataBase.GPSInfo;
import com.example.maplocationdemo.DataBase.PoiInfo;

import java.util.ArrayList;
import java.util.List;

public class ConnectionTaskService extends Service {

    private final IBinder binder = new MyBinder();

    private List<AsyncTask<Void, Void, Void>> taskList = new ArrayList<>();

    public class MyBinder extends Binder {
        public ConnectionTaskService getService() {
            return ConnectionTaskService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void startAddressTask(Context context, Address address) {
        AddressConnectionTask addressConnectionTask = new AddressConnectionTask();
        addressConnectionTask.setContext(context);
        addressConnectionTask.setAddress(address);
        addressConnectionTask.execute();
        taskList.add(addressConnectionTask);
    }

    public void startPoiInfoTask(PoiInfo poiInfo) {
        PoiInfoConnectionTask poiInfoConnectionTask = new PoiInfoConnectionTask();
        poiInfoConnectionTask.setPoiInfo(poiInfo);
        poiInfoConnectionTask.execute();
        taskList.add(poiInfoConnectionTask);
    }
    public void startGPSInfoTask(GPSInfo gpsInfo) {
        GPSInfoConnectionTask gpsInfoConnectionTask = new GPSInfoConnectionTask();
        gpsInfoConnectionTask.setGPSInfo(gpsInfo);
        gpsInfoConnectionTask.execute();
        taskList.add(gpsInfoConnectionTask);
    }




    public void stopTask() {
        if (!taskList.isEmpty()) {
            AsyncTask<Void, Void, Void> task = taskList.remove(0);
            task.cancel(true);
        }
    }

}

