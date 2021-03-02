package com.example.weatherboi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherboi.adapter.DailyAdapter;
import com.example.weatherboi.beans.City;
import com.example.weatherboi.beans.Daily;
import com.example.weatherboi.http.OkHttpHelper;
import com.example.weatherboi.http.SpotsCallback;
import com.example.weatherboi.utils.GPS;
import com.example.weatherboi.utils.JSONUtil;
import com.example.weatherboi.utils.SnackbarUtil;
import com.example.weatherboi.utils.ToastUtils;
import com.example.weatherboi.viewpager.CommonViewPager;
import com.example.weatherboi.viewpager.ViewPagerHolder;
import com.example.weatherboi.viewpager.ViewPagerHolderCreator;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruffian.library.RTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private CommonViewPager mCommonViewPager;
    private GPS gps;
    private static final String TAG = "GPS-Info";
    private SpotsDialog mDialog;

    @ViewInject(R.id.main_srl)
    private SmartRefreshLayout smartRefreshLayout;
    private static City my_city;
    private List<City> cities = new ArrayList<>();
    private int task_no = 0;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            SnackbarUtil.ShortSnackbar(getWindow().getDecorView(), "Locate permission denied", SnackbarUtil.Warning).show();
            initCityList();
        } else {
            locate();
        }
    }

    private void initCityList() {
        cities.clear();
        if (my_city != null) {
            cities.add(my_city);
        }
        cities.add(new City(Constants.SYD_LAT, Constants.SYD_LON, R.drawable.syd));
        cities.add(new City(Constants.PER_LAT, Constants.PER_LON, R.drawable.perth));
        cities.add(new City(Constants.HBT_LAT, Constants.HBT_LON, R.drawable.hbt));
        task_no = cities.size();
        for (int i = 0; i < cities.size(); i++) {
            fetchWeather(cities.get(i));
        }
    }

    private void fetchWeather(final City city) {
        Map<String, String> params = new HashMap<>();
        params.put("lat", city.getLat() + "");
        params.put("lon", city.getLon() + "");
        params.put("units", "metric");
        params.put("exclude", "minutely,hourly");
        params.put("appid", Constants.API_KEY);
        okHttpHelper.get(Constants.API.ONE_CALL, params, new SpotsCallback<String>(this) {
            @Override
            public void onSuccess(Response response, String s) {
                super.onSuccess(response, s);
                try {
                    JSONObject oneCallJson = new JSONObject(s);
                    city.setName(oneCallJson.optString("timezone"));
                    String current = oneCallJson.optString("current", null);
                    if (!TextUtils.isEmpty(current)) {
                        JSONObject curJson = new JSONObject(current);
                        JSONArray weather = curJson.getJSONArray("weather");
                        JSONObject cur_weather = (JSONObject) weather.get(0);
                        city.setToday_weather_desc(cur_weather.optString("main"));
                        city.setCurrent_temp(curJson.optDouble("temp", 0));
                    }

                    JSONArray dailyArray = oneCallJson.getJSONArray("daily");
                    List<Daily> dailyList = new ArrayList<>();
                    JSONObject today = (JSONObject) dailyArray.get(0);
                    JSONObject today_temp = (JSONObject) today.getJSONObject("temp");
                    city.setToday_min_temp(today_temp.optDouble("min"));
                    city.setToday_max_temp(today_temp.optDouble("max"));
                    for (int i = 0; i < dailyArray.length(); i++) {
                        JSONObject daily = (JSONObject) dailyArray.get(i);
                        Daily my_daily = JSONUtil.fromJson(daily.toString(), Daily.class);
                        dailyList.add(my_daily);
//                        Log.e("daily",daily.toString());
                    }
                    city.setDailyList(dailyList);
                    task_no--;
                    if (task_no == 0) {
                        initView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Failed Loading")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fetchWeather(city);
                            }
                        })
                        .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                super.onError(response, code, e);
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Failed Loading")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fetchWeather(city);
                            }
                        })
                        .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
            }
        });
    }


    private void locate() {
        gps = new GPS(this);
        mDialog = new SpotsDialog(this, "Locating...");
        mDialog.show();
        gps.setMyLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e(TAG, "onLocationChanged");
                Log.e(TAG, "latitude" + location.getLatitude());
                Log.e(TAG, "Longitude" + location.getLongitude());
                mDialog.dismiss();
                my_city = new City(location.getLatitude(), location.getLongitude(), R.drawable.mycty);
                initCityList();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });
        gps.startLocation();

    }

    private void initView() {
        mCommonViewPager = (CommonViewPager) findViewById(R.id.activity_common_view_pager);
        // 设置数据
        mCommonViewPager.setPages(cities, new ViewPagerHolderCreator<ViewImageHolder>() {
            @Override
            public ViewImageHolder createViewHolder() {
                // 返回ViewPagerHolder
                return new ViewImageHolder();
            }
        });
        mCommonViewPager.setCurrentItem(currentPage);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = mCommonViewPager.getCurrentItem();
                initCityList();
                smartRefreshLayout.finishRefresh();
            }
        });
    }

    /**
     * 提供ViewPager展示的ViewHolder
     * <P>用于提供布局和绑定数据</P>
     */
    public static class ViewImageHolder implements ViewPagerHolder<City> {
        private RTextView cityName;
        private TextView curTemp;
        private TextView todayTemp;
        private TextView weatherDesc;
        private RecyclerView dailyRV;
        private TextView moreBTN;
        private NestedScrollView scrollView;

        @Override
        public View createView(Context context) {
            // 返回ViewPager 页面展示的布局
            View view = LayoutInflater.from(context).inflate(R.layout.view_pager_item, null);
            cityName = (RTextView) view.findViewById(R.id.item_desc);
            curTemp = (TextView) view.findViewById(R.id.item_cur_temp);
            todayTemp = (TextView) view.findViewById(R.id.item_today_temp);
            weatherDesc = (TextView) view.findViewById(R.id.item_today_weather);
            dailyRV = (RecyclerView) view.findViewById(R.id.item_rv);
            moreBTN = (TextView) view.findViewById(R.id.more_tv);
            scrollView = (NestedScrollView) view.findViewById(R.id.item_nsv);
            return view;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBind(final Context context, int position, final City city) {
            // 数据绑定
            if (my_city != null && city.getName().equals(my_city.getName())) {
                cityName.setIconNormal(MyApplication.getInstance().getDrawable(R.drawable.location));
            }
            cityName.setText(city.getName());
            curTemp.setText(String.format("%s", city.getCurrent_temp()));
            todayTemp.setText(String.format("%s / %s", city.getToday_min_temp(), city.getToday_max_temp()));
            weatherDesc.setText(city.getToday_weather_desc());
            if (city.getThreeDayList() == null) {
                ToastUtils.show(context, "no data");
                return;
            }
            DailyAdapter dailyAdapter = new DailyAdapter(context, city.getThreeDayList());
            dailyRV.setAdapter(dailyAdapter);
            dailyRV.setLayoutManager(new LinearLayoutManager(context));
            dailyRV.setItemAnimator(new DefaultItemAnimator());
            dailyRV.setHasFixedSize(true);
            dailyRV.setNestedScrollingEnabled(false);
            scrollView.setBackground(MyApplication.getInstance().getDrawable(city.getBg_id()));
            moreBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SevenDaysActivity.class);
                    String city_json = JSONUtil.toJson(city);
                    intent.putExtra("city_json", city_json);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gps != null) {
            gps.stopLocation();
        }
    }
}
