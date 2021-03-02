package com.example.weatherboi.beans;

import java.text.DecimalFormat;
import java.util.List;

public class City {
    private String name;
    private int bg_id;
    private double lat;
    private double lon;
    private double current_temp;
    private double today_max_temp;
    private double today_min_temp;
    private String today_weather_desc;
    private List<Daily> dailyList;

    public City(){

    }
    public City(double lat, double lon, int bg_id) {
        this.lat = lat;
        this.lon = lon;
        this.bg_id = bg_id;
    }

    public String getToday_max_temp() {
        return new DecimalFormat("0").format(today_max_temp)+"°";
    }

    public void setToday_max_temp(double today_max_temp) {
        this.today_max_temp = today_max_temp;
    }

    public String getToday_min_temp() {
        return new DecimalFormat("0").format(today_min_temp)+"°";
    }

    public void setToday_min_temp(double today_min_temp) {
        this.today_min_temp = today_min_temp;
    }

    public String getToday_weather_desc() {
        return today_weather_desc;
    }

    public void setToday_weather_desc(String today_weather_desc) {
        this.today_weather_desc = today_weather_desc;
    }

    public int getBg_id() {
        return bg_id;
    }

    public void setBg_id(int bg_id) {
        this.bg_id = bg_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCurrent_temp() {
        return new DecimalFormat("0").format(current_temp)+"°";
    }

    public void setCurrent_temp(double current_temp) {
        this.current_temp = current_temp;
    }

    public List<Daily> getDailyList() {
        return dailyList;
    }
    public List<Daily> getThreeDayList(){
        if(dailyList.size()>3){
           return dailyList.subList(1,4);
        }
        return null;
    }
    public void setDailyList(List<Daily> dailyList) {
        this.dailyList = dailyList;
    }
}
