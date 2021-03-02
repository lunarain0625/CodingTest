package com.example.weatherboi.http;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHelper {

    public static final int TOKEN_MISSING = 401;
    public static final int TOKEN_ERROR = 402;
    public static final int TOKEN_EXPIRE = 403;

    public static final String TAG = "OkHttpHelper";

    private static OkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;

    private Handler mHandler;

    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper() {

        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        mGson = new Gson();

        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance() {
        return mInstance;
    }

    public void get(String url, Map<String, String> param, BaseCallback callback) {
        Request request = buildGetRequest(url, param);
        request(request, callback);
    }

    public void get(String url, BaseCallback callback) {
        get(url, null, callback);
    }

    public void post(String url, Map<String, String> param, BaseCallback callback) {
        Request request = buildPostRequest(url, param);
        request(request, callback);
    }

    public void files(String url, Map<String, String> param, BaseCallback callback) {
        Request request = buildFilesRequest(url, param);
        request(request, callback);
    }

    private Request buildFilesRequest(String url, Map<String, String> param) {
        return buildRequest(url, HttpMethodType.FILES, param);
    }

    private Request buildPostRequest(String url, Map<String, String> param) {
        return buildRequest(url, HttpMethodType.POST, param);
    }

    private void request(final Request request, final BaseCallback callback) {
        callback.onBeforeRequset(request);

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackResponse(callback, response);
                String resultStr = response.body().string();
                Log.d(TAG,"result="+resultStr);
                if (response.isSuccessful()) {
//                    String resultStr = response.body().string();
//                    Log.d(TAG,"result="+resultStr);
                    if (callback.mType == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object object = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback, response, object);
                        } catch (JsonParseException e) {
                            callback.onError(response, response.code(), e);
                        }
                    }
                } else if (response.code() == TOKEN_MISSING || response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE) {
                    callbackTokenError(callback, response);
                } else {
                    callbackError(callback, response, null);
                }
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }

    private void callbackTokenError(final BaseCallback callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
            }
        });
    }

    private void callbackResponse(final BaseCallback callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }

    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }

    private Request buildGetRequest(String url, Map<String, String> param) {
        return buildRequest(url, HttpMethodType.GET, param);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String, String> param) {
        url = fillUrl(url);
        Request.Builder builder = new Request.Builder().url(url);
//        String ua = MyApplication.getInstance().getUserAgent();
//        if (TextUtils.isEmpty(ua)) {
//            ua = Constans.APPEND_USER_AGENT;
//            MyApplication.getInstance().putUserAgent(ua);
//        }

        builder.header("User-Agent", "mobile");
//        if(MyApplication.getInstance().getAuth()!=null){
//        builder.header("Authorization", MyApplication.getInstance().getAuth());}

        if (methodType == HttpMethodType.POST) {
            RequestBody body = builderFormBody(param);
            builder.post(body);
        } else if (methodType == HttpMethodType.GET) {
            url = buildUrlParams(url, param);
            builder.url(url);

            builder.get();
        } else if (methodType == HttpMethodType.FILES) {
            RequestBody body = builderFileBody(param);
            builder.post(body);
        }

        Log.d(TAG, "requestUrl=" + url);
        return builder.build();
    }

    private String fillUrl(String url) {
        String nUrl = url;

        StringBuffer sb = new StringBuffer();
//        String token = MyApplication.getInstance().getToken();
//        if (!TextUtils.isEmpty(token)) {
//            sb.append("token=" + token);
//        }
//        String auth = MyApplication.getInstance().getAuth();
//        if (!TextUtils.isEmpty(auth)) {
////            sb.append("&auth=" + auth);
//        }

        String s = sb.toString();
        if (nUrl.indexOf("?") > 0) {
            nUrl = nUrl + "&" + s;
        } else {
            nUrl = nUrl + "?" + s;
        }

        return nUrl;
    }

    private RequestBody builderFileBody(Map<String, String> fileMap) {

        File file = new File(fileMap.get("filePath"));
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("application/octet-stream", file.getName(), fileBody);
        for (Map.Entry<String, String> entry : fileMap.entrySet()
        ) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    private RequestBody builderFormBody(Map<String, String> param) {
        FormBody.Builder builder = new FormBody.Builder();

        if (param != null) {
            for (Map.Entry<String, String> entry : param.entrySet()
            ) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();
    }

    private String buildUrlParams(String url, Map<String, String> param) {

        if (param == null) {
            param = new HashMap<>(1);
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if (url.indexOf("?") > 0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }

        return url;
    }


    enum HttpMethodType {
        GET,
        POST,
        FILES,
    }
}
