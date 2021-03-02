package com.example.weatherboi.http;

import android.content.Context;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;


public abstract class SpotsCallback<T> extends SimpleCallback<T> {

    private SpotsDialog mDialog;

    public SpotsCallback(Context context) {
        super(context);
        initSpotsDialog();
    }

    public void initSpotsDialog(){
        mDialog = new SpotsDialog(mContext, "Loading...");
    }

    public void showDialog(){
        mDialog.show();
    }

    public void dismissDialog(){
        mDialog.dismiss();
    }

    @Override
    public void onBeforeRequset(Request request) {
        showDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        super.onFailure(request, e);
        dismissDialog();
    }

    public void setLoadMessage(int resID){
        mDialog.setMessage(mContext.getString(resID));
    }

}
