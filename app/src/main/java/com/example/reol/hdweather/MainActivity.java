package com.example.reol.hdweather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reol.hdweather.Entity.WeatherInfo;
import com.example.reol.hdweather.Utils.Constant;
import com.example.reol.hdweather.Utils.HttpUtils;
import com.example.reol.hdweather.Utils.WeatherUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "hdtest";

    TextView tvTodayInfo;
    TextView tvTodayTmp;
    TextView tvSecondInfo;
    TextView tvSecondTitle;
    TextView tvThirdTitle;
    TextView tvThirdInfo;
    ActionBar actionBar;
    WeatherUtils wutil = new WeatherUtils();



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (msg.obj != null) {
                    WeatherInfo info = (WeatherInfo) msg.obj;
                    setInfo(info);
                }
            }

        }

    };

    private void setInfo(WeatherInfo info) {
        tvTodayTmp.setText("");
        tvTodayInfo.setText("");
        tvSecondTitle.setText("");
        tvSecondInfo.setText("");
        tvThirdTitle.setText("");
        tvThirdInfo.setText("");
        actionBar.setTitle(wutil.getCityName(info));
        tvTodayTmp.append(wutil.getNowTmp(info) + "ºC");
        tvTodayInfo.append(wutil.getInfo(info,0, Constant.TMPMIN) + "ºC/" +
                wutil.getInfo(info,0,Constant.TMPMAX) + "ºC" + "\r\n");
        tvTodayInfo.append(wutil.getInfo(info,0,Constant.DAY) + " / " +
                wutil.getInfo(info,0,Constant.NIGHT)+ "\r\n");
        tvTodayInfo.append("降水概率: " + wutil.getInfo(info,0,Constant.POP) + "%" + "\r\n");
        tvTodayInfo.append("PM2.5: " + wutil.getPM25(info));


        tvSecondTitle.append(wutil.getInfo(info,1,Constant.DATE) + "\r\n");
        tvSecondInfo.append(wutil.getInfo(info,1,Constant.TMPMIN) + "ºC/" +
                wutil.getInfo(info,1,Constant.TMPMAX)+ "ºC" + "\r\n");
        tvSecondInfo.append(wutil.getInfo(info,1,Constant.DAY) + " / " +
                wutil.getInfo(info,1,Constant.NIGHT) + "\r\n");
        tvSecondInfo.append("降水概率: " + wutil.getInfo(info,1,Constant.POP) + "%");

        tvThirdTitle.append(wutil.getInfo(info,2,Constant.DATE) + "\r\n");

        tvThirdInfo.append(wutil.getInfo(info,2,Constant.TMPMIN) + "ºC/" +
                wutil.getInfo(info,2,Constant.TMPMAX) + "ºC" + "\r\n");
        tvThirdInfo.append(wutil.getInfo(info,2,Constant.DAY) + " / " +
                wutil.getInfo(info,2,Constant.NIGHT) + "\r\n");
        tvThirdInfo.append("降水概率: " + wutil.getInfo(info,2,Constant.POP) + "%");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        initView();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        refreshInfo("CN101030100");
    }

    private void refreshInfo(final String cityId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = Message.obtain();
                msg.what = 1;

                HttpUtils http = new HttpUtils();
                WeatherInfo info = null;
                try {
                    info = http.json2weather(http.getWeatherInfo(cityId));
//                    Log.d(TAG, "run: "+http.getWeatherInfo());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (info != null) {
                    msg.obj = info;
                } else {
                    msg.obj = null;
                }

                handler.sendMessage(msg);
            }
        }).start();
    }

    private void initView() {
        tvTodayInfo = (TextView) findViewById(R.id.tv_today_info);
        tvTodayTmp = (TextView) findViewById(R.id.tv_today_tmp);
        tvSecondTitle = (TextView) findViewById(R.id.tv_second_title);
        tvSecondInfo = (TextView) findViewById(R.id.tv_second_info);
        tvThirdTitle = (TextView) findViewById(R.id.tv_third_title);
        tvThirdInfo = (TextView) findViewById(R.id.tv_third_info);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            if (isNetworkConnected(this)){
                refreshInfo("CN101030100");
                Toast.makeText(MainActivity.this, "正在更新数据…", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_city) {
            startActivity(new Intent(MainActivity.this,ChangeCityActivity.class));

        } else if (id == R.id.nav_change_bg) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_ctymain) {
            if (!"天津".equals(actionBar.getTitle())){
                refreshInfo("CN101030100");
            }

        } else if (id == R.id.nav_ctyanother) {
            if (!"牡丹江".equals(actionBar.getTitle())){
                refreshInfo("CN101050301");
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
