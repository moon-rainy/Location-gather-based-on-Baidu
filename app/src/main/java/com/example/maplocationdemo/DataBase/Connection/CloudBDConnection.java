package com.example.maplocationdemo.DataBase.Connection;

import com.example.maplocationdemo.DataBase.Address;
import com.example.maplocationdemo.DataBase.GPSInfo;
import com.example.maplocationdemo.DataBase.PoiInfo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CloudBDConnection {
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "XXXXXXXXXXXXXXXXX";
    private static final String userName = "XXXXXXXXX";
    private static final String password = "XXXXXXXXXXXXXXx";
    public static Connection getConnection() {
        Connection connection = null;
        try{
            Class.forName(driver);
            connection = (Connection) DriverManager.getConnection(url, userName, password);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }


    public static void linkAddress(Address address) {
        Connection connection = getConnection();
        if (connection == null) {
            System.out.println("获取数据库连接失败！");
            return;
        }

        try {
            String sql = "INSERT INTO address (code, Phone, time, locType, locTypeDescription, latitude, longitude, height, radius, Province, citycode, city, District, Town, Street, addr, StreetNumber, UserIndoorState, Direction, locationdescribe) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, address.getCode());
                ps.setString(2, address.getPhone());
                ps.setString(3, address.getTime());
                ps.setInt(4, address.getLocType());
                ps.setString(5, address.getLocTypeDescription());
                ps.setDouble(6, address.getLatitude());
                ps.setDouble(7, address.getLongitude());
                ps.setDouble(8, address.getHeight());
                ps.setDouble(9, address.getRadius());
                ps.setString(10, address.getProvince());
                ps.setString(11, address.getCityCode());
                ps.setString(12, address.getCity());
                ps.setString(13, address.getDistrict());
                ps.setString(14, address.getTown());
                ps.setString(15, address.getStreet());
                ps.setString(16, address.getAddr());
                ps.setString(17, address.getStreetNumber());
                ps.setInt(18, address.getUserIndoorState());
                ps.setString(19, address.getDirection());
                ps.setString(20, address.getLocationDescribe());

                ps.executeUpdate();
                System.out.println("数据插入成功！！！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void linkPoiInfo(PoiInfo poiInfo) {
        Connection connection = getConnection();

        try {
            String sql = "INSERT INTO poiinfo (code, PoiName, PoiTag) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, poiInfo.getCode());
                ps.setString(2, poiInfo.getPoiName());
                ps.setString(3, poiInfo.getPoiATag());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void linkGPSInfo(GPSInfo gpsInfo) {
        Connection connection = getConnection();

        try {
            String sql = "INSERT INTO gpsinfo (code, speed, satellite, height, status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, gpsInfo.getCode());
                ps.setDouble(2, gpsInfo.getSpeed());
                ps.setInt(3, gpsInfo.getSatellite());
                ps.setDouble(4, gpsInfo.getHeight());
                ps.setInt(5, gpsInfo.getStatus());


                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

