package com.example.weatherboi.beans;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Daily {
    private long dt;
    private List<Weather> weather;
    private Temp temp;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    @SuppressLint("SimpleDateFormat")
    public String getDate() {
        Date date = new Date(dt * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar today = Calendar.getInstance();
        Calendar tmrw = Calendar.getInstance();
        tmrw.add(Calendar.DAY_OF_MONTH, 1);
        if (today.compareTo(calendar) >= 0) {
            return ("Today, " + new SimpleDateFormat("dd MMM").format(date));
        } else if (tmrw.compareTo(calendar)>=0) {
            return ("Tomorrow, " + new SimpleDateFormat("dd MMM").format(date));
        } else {
            return (new SimpleDateFormat("E, dd MMM").format(date));
        }
    }
}
