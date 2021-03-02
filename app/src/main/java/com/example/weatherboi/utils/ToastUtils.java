package com.example.weatherboi.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;


public class ToastUtils {

    public static void show(Context context, int resID){
        show(context, context.getResources().getText(resID), Toast.LENGTH_LONG);
    }

    public static void show(Context context, int resID, int duration){
        show(context, context.getResources().getText(resID), duration);
    }

    public static void show(Context context, CharSequence text){
        show(context, text, Toast.LENGTH_SHORT);
    }

    private static void show(Context context, CharSequence text, int lengthLong) {
        try {
            Toast.makeText(context, text, lengthLong).show();
        }catch (Exception e){
            Looper.prepare();
            Toast.makeText(context, text, lengthLong).show();
            Looper.loop();
        }
    }

    public static void show(Context context, int resID, Object... args){
        show(context, Toast.LENGTH_SHORT, String.format(context.getResources().getString(resID), args));
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_LONG);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }
}
