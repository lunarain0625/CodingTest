package com.example.weatherboi.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.weatherboi.R;
import com.example.weatherboi.beans.Daily;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class DailyAdapter extends SimpleAdapter<Daily> {

    public DailyAdapter(Context context, List<Daily> data) {
        super(context, R.layout.template_daily, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Daily item) {
        holder.getTextView(R.id.item_date).setText(item.getDate());
        holder.getTextView(R.id.item_temp).setText(String.format("%s / %s", item.getTemp().getMin(), item.getTemp().getMax()));
        SimpleDraweeView icon = (SimpleDraweeView) holder.getView(R.id.item_weather_icon);
        if(item.getWeather().get(0)!=null){
            Uri uri = Uri.parse("https://openweathermap.org/img/wn/"+item.getWeather().get(0).getIcon()+"@2x.png");
            Log.v("uri_icon",""+uri);
            icon.setImageURI(uri);
        }
    }
}