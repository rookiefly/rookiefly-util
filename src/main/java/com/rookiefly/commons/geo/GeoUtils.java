package com.rookiefly.commons.geo;

public class GeoUtils {
    private static final double EARTH_RADIUS = 6378137;//赤道半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    public static void main(String[] args) {
        double lon = 111.59614108038215;
        double lat = 40.736434781407354;
        double lon1 = 111.62079875;
        double lat1 = 40.75164775;
        double dist = GeoUtils.getDistance(lon, lat, lon1, lat1);
        System.out.println("两点相距：" + dist + " 米");
    }
}
