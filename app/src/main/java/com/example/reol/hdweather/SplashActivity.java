package com.example.reol.hdweather;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reol.hdweather.Entity.CityInfo;
import com.example.reol.hdweather.Utils.HttpUtils;
import com.example.reol.hdweather.db.DatabaseHelper;

import java.io.IOException;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    TextView tvLoading;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                Toast.makeText(SplashActivity.this, "数据写入完成", Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("first_check", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("times", 1);
                editor.apply();
                enterHome();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        dbHelper = new DatabaseHelper(this, "CityInfo.db", null, 1);

        initView();
        initDatabase();
    }

    private void initDatabase() {
        SharedPreferences sp = getSharedPreferences("first_check", MODE_PRIVATE);

        int a = sp.getInt("times", 0);
        if (a == 0) {
            //refresh Database
            tvLoading.setVisibility(View.VISIBLE);
            refreshCityInfo();

        } else if (a == 1) {
            //enter home post delay 3000
            enterHome(3000);
        }
    }

    private void enterHome() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void enterHome(long delayMills) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, delayMills);

    }

    private void initView() {
        tvLoading = (TextView) findViewById(R.id.tv_loading);
    }


    private void refreshCityInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = Message.obtain();
                msg.what = 1001;

                HttpUtils http = new HttpUtils();
                CityInfo cityInfo;
                try {
                    cityInfo = http.json2city(http.getCityInfo());

                    List<CityInfo.CityInfoBean> list = cityInfo.getCity_info();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues(1000);
                    for (CityInfo.CityInfoBean cityInfoBean :
                            list) {
                        values.put("cityid", cityInfoBean.getId());
                        values.put("cityname", cityInfoBean.getCity());
                        values.put("province", cityInfoBean.getProv());
                        db.insert("CityInfo", null, values);
                        values.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(msg.what);
            }
        }).start();
    }
}
