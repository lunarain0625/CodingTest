package com.example.weatherboi.beans;

import java.text.DecimalFormat;

public class Temp {
    private double min;
    private double max;

    public String getMin() {
        return new DecimalFormat("0").format(min)+"°";
    }

    public void setMin(double min) {
        this.min = min;
    }

    public String getMax() {
        return new DecimalFormat("0").format(max)+"°";
    }

    public void setMax(double max) {
        this.max = max;
    }
}
