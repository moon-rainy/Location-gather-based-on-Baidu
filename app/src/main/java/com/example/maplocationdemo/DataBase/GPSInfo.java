package com.example.maplocationdemo.DataBase;

public class GPSInfo {
    private String code;    //唯一标识码
    private double speed;   // 速度 单位：km/h
    private int satellite;  // 卫星数目
    private double height;  // 海拔高度 单位：米
    private int status;  // *****gps质量判断*****

    public GPSInfo() {
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSatellite() {
        return satellite;
    }

    public void setSatellite(int satellite) {
        this.satellite = satellite;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GPSInfo{" +
                "code='" + code + '\'' +
                ", speed=" + speed +
                ", satellite=" + satellite +
                ", height=" + height +
                ", status=" + status +
                '}';
    }
}
