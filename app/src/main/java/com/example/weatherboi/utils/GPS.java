package com.example.weatherboi.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class GPS {
    private Context context;
    private LocationManager locationManager;
    private static final String TAG = "GPS-Info";

    public GPS(Context context) {
        this.context = context;
        initLocationManager();
    }

    /**
     * 获取权限，并检查有无开户GPS
     */
    private void initLocationManager() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        getProviders();
    }

    /**
     * 获取可定位的方式
     */
    private LocationListener myLocationListener;
    private String bestProvider;

    private void getProviders() {
        //获取定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String s : providers) {
            Log.e(TAG, s);
        }

        Criteria criteria = new Criteria();
        // 查询精度：高，Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精确
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(true);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        bestProvider = locationManager.getBestProvider(criteria, false);  //获取最佳定位

    }

    public void startLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.e(TAG, "startLocation: ");
        locationManager.requestLocationUpdates(bestProvider, 100, 100,myLocationListener);
    }

    public void stopLocation(){
        Log.e(TAG, "stopLocation: ");
        locationManager.removeUpdates(myLocationListener);

    }
    public void setMyLocationListener(LocationListener myLocationListener){
        this.myLocationListener = myLocationListener;
    }
}
