package com.example.helloworldd;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;


public class LocationUtils {
    // 纬度
    public static Double latitude = 0.0;
    // 经度
    public static Double longitude = 0.0;

    /**
     * 初始化位置信息
     *
     * @param context
     */
    public static void initLocation(Context context) {
        LocationListener locationListener = new LocationListener() {

            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {

            }

            // Provider被enable时触发此函数，比如GPS被打开
            @Override
            public void onProviderEnabled(String provider) {

            }

            // Provider被disable时触发此函数，比如GPS被关闭
            @Override
            public void onProviderDisabled(String provider) {

            }

            // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        };

        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        System.out.println("********locationManager:"+locationManager);

        // 检测GPS定位是否可用，不可用则进行网络定位（信号塔或WiFi定位的初略定位方式）
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            System.out.println("********locationManager.isProviderEnabled:"+LocationManager.GPS_PROVIDER);
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Location location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                System.out.println("********initLocation-->isProviderEnabled-->after-->latitude:"+latitude);
                System.out.println("********initLocation-->isProviderEnabled-->after-->longitude:"+longitude);
            }
        } else {

            locationManager
                    .requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000, 0, locationListener);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度
            }
            System.out.println("********initLocation-->after-->latitude:"+latitude);
            System.out.println("********initLocation-->after-->longitude:"+longitude);
        }
    }
}
