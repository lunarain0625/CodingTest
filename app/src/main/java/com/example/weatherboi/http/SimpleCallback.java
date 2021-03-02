package com.example.weatherboi.http;

import android.content.Context;

import okhttp3.Request;
import okhttp3.Response;


public class SimpleCallback<T> extends BaseCallback<T> {

    protected Context mContext;

    public SimpleCallback(Context context) {
        this.mContext = context;
    }

    @Override
    public void onBeforeRequset(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onTokenError(Response response, int code) {
    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onSuccess(Response response, T t) {

    }

    @Override
    public void onError(Response response, int code, Exception e) {

    }
}
