package com.example.maplocationdemo.DataBase.Connection;

import com.example.maplocationdemo.DataBase.Address;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CloudBDConnection {
    private static final String driver = "${yourdiver}";
    private static final String url = "${yoururl}";
    private static final String userName = "${youruserName}";
    private static final String password = "${yourpassword}";
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


    public static void link(Address address) {
        Connection connection = getConnection();
        if (connection == null) {
            System.out.println("获取数据库连接失败！");
            return;
        }

        try {
            String sql = "INSERT INTO address (Phone, time, locType, locTypeDescription, latitude, longitude, radius, Province, citycode, city, District, Town, Street, addr, StreetNumber, UserIndoorState, Direction, locationdescribe) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, address.getPhone());
                ps.setString(2, address.getTime());
                ps.setInt(3, address.getLocType());
                ps.setString(4, address.getLocTypeDescription());
                ps.setDouble(5, address.getLatitude());
                ps.setDouble(6, address.getLongitude());
                ps.setDouble(7, address.getRadius());
                ps.setString(8, address.getProvince());
                ps.setString(9, address.getCityCode());
                ps.setString(10, address.getCity());
                ps.setString(11, address.getDistrict());
                ps.setString(12, address.getTown());
                ps.setString(13, address.getStreet());
                ps.setString(14, address.getAddr());
                ps.setString(15, address.getStreetNumber());
                ps.setInt(16, address.getUserIndoorState());
                ps.setString(17, address.getDirection());
                ps.setString(18, address.getLocationDescribe());

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


}

