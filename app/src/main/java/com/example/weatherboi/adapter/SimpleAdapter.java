package com.example.weatherboi.adapter;

import android.content.Context;

import java.util.List;

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {
    public SimpleAdapter(Context context, int layoutResid) {
        super(context, layoutResid);
    }

    public SimpleAdapter(Context context, int layoutResid, List<T> datas) {
        super(context, layoutResid, datas);
    }

}
