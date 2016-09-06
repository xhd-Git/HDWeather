package com.example.reol.hdweather;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.reol.hdweather.Entity.WeatherInfo;
import com.example.reol.hdweather.Utils.HttpUtils;

import java.io.IOException;

public class ChangeCityActivity extends AppCompatActivity {

    TextView tv;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (msg.obj != null) {
                    tv.setText((CharSequence) msg.obj);

                }
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_change_city);

        tv = (TextView) findViewById(R.id.tv_test);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = Message.obtain();
                msg.what = 1;

                HttpUtils http = new HttpUtils();
                String ss = null;
                try {
                    ss = http.getCityInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (ss != null) {
                    msg.obj = ss;
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        }).start();
    }
}
