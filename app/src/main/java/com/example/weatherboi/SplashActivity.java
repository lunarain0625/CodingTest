package com.example.weatherboi;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.weatherboi.utils.CheckPermission;
import com.example.weatherboi.utils.NetStateUtil;
import com.example.weatherboi.utils.SnackbarUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        checkConnection();
    }

    private void checkConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetStateUtil netStateUtil = new NetStateUtil();
                String status = netStateUtil.connectingAddress(Constants.API.SITE_URL);
                if (status == "异常") {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            initDialog();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                requestLocatePermission();
                            }else {
                                mainThreadContinue();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void initDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("No Internet Access")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkConnection();
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    private void mainThreadContinue() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 0);
    }

    private void requestLocatePermission() {
        final String[] strings = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        new CheckPermission(this).requestPermission(strings, new CheckPermission.PermissionLinstener() {
            @Override
            public void onSuccess(Context context, List<String> data) {
                SnackbarUtil.ShortSnackbar(getWindow().getDecorView(),"Allowed",SnackbarUtil.Confirm).show();
                mainThreadContinue();
            }

            @Override
            public void onFailed(Context context, List<String> data) {
                SnackbarUtil.ShortSnackbar(getWindow().getDecorView(),"Denied",SnackbarUtil.Alert).show();
                mainThreadContinue();
            }

            @Override
            public void onNotApply(final Context context, List<String> data) {
                SnackbarUtil.ShortSnackbar(getWindow().getDecorView(),"No More Apply",SnackbarUtil.Alert).show();
                mainThreadContinue();
            }
        });
    }


    /**
     * 转主页
     **/
    private void enterHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}