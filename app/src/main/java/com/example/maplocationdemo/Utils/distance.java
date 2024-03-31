package com.example.maplocationdemo.Utils;

public class distance {
    private static final int EARTH_RADIUS = 6371; // 地球半径（单位：千米）

    public static double calculateDistance(double startLat, double startLong, double startAltitude,
                                           double endLat, double endLong, double endAltitude) {

        double dLat = Math.toRadians(endLat - startLat);
        double dLong = Math.toRadians(endLong - startLong);

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLong / 2), 2) *
                        Math.cos(startLat) *
                        Math.cos(endLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算两点间的距离（不考虑高度）
        double distance = EARTH_RADIUS * c;

        // 将海拔差转换成距离
        double height = startAltitude - endAltitude;

        // 计算考虑高度差后的距离
        distance = Math.pow(distance, 2) + Math.pow(height / 1000, 2);
        return Math.sqrt(distance);
    }
}
