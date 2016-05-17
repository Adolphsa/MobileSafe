package com.zividig.mobilesafe.activity.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * 定位服务
 * Created by Administrator on 2016-05-17.
 */
public class LocationService extends Service {

    private MyListener listener;
    private LocationManager locationManager;
    private SharedPreferences sp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("config", MODE_PRIVATE);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        List<String> allProviders = locationManager.getAllProviders(); //获取所有位置的提供者
//        System.out.println(allProviders);

        listener = new MyListener();

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        System.out.println("bestProvider:" + bestProvider);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(listener);
    }

    class MyListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) { //位置更新的时候调用
            double longitude = location.getLongitude(); //经度
            double latitude = location.getLatitude(); //纬度
            float accuracy = location.getAccuracy(); //精度

            sp.edit().putString("location","j:" + location.getLongitude() + "\n" + "w:" + location.getLatitude()).apply();

            System.out.println("经度:" + longitude + "\n" + "纬度:" + latitude + "\n" + "精度:" + accuracy);

            stopSelf();//拿到经纬度后停止服务   减少电量的消耗
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { //Gps的状态发生改变的时候调用
            System.out.println("Gps的状态发生了改变");
        }

        @Override
        public void onProviderEnabled(String provider) { //Gps打开的时候调用
            System.out.println("Gps已打开");
        }

        @Override
        public void onProviderDisabled(String provider) { //Gps关闭的时候调用
            System.out.println("Gps已关闭");
        }
    }
}
