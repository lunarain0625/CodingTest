package com.example.weatherboi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.weatherboi.adapter.DailyAdapter;
import com.example.weatherboi.beans.City;
import com.example.weatherboi.utils.JSONUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruffian.library.RTextView;

public class SevenDaysActivity extends AppCompatActivity {

    @ViewInject(R.id.back_btn)
    private RTextView backBtn;
    @ViewInject(R.id.title_tv)
    private TextView cityName;
    @ViewInject(R.id.seven_days_rv)
    private RecyclerView recyclerView;
    @ViewInject(R.id.root_layout)
    private ConstraintLayout rootLayout;

    private City city;
    private DailyAdapter dailyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven_days);
        ViewUtils.inject(this);
        city = JSONUtil.fromJson(getIntent().getStringExtra("city_json"),City.class);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(city!=null){
            initView();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView() {
        cityName.setText(city.getName());
        dailyAdapter = new DailyAdapter(this, city.getDailyList());
        recyclerView.setAdapter(dailyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        rootLayout.setBackground(MyApplication.getInstance().getDrawable(city.getBg_id()));
    }
}
