package com.example.reol.hdweather.Utils;

import android.util.Log;
import android.widget.TextView;

import com.example.reol.hdweather.Entity.CityInfo;
import com.example.reol.hdweather.Entity.WeatherInfo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络连接工具类
 * 输入信息为城市id
 * 输出为天气信息
 * <p/>
 * <p/>
 * Created by reol on 16-8-27.
 */
public class HttpUtils {
    private static final String API_KEY = "6057e51c3d4f4fb5808af84fcf8a4b0f ";
    private static final String URL = "https://api.heweather.com/x3/weather";

    private static String citylist = "https://api.heweather.com/x3/citylist?search=allchina&key=" + API_KEY;

    OkHttpClient client = new OkHttpClient();


    public String getWeatherInfo(String cityId) throws IOException {

        Request request = new Request.Builder()
                .url(URL + "?cityid="+cityId+"&key=" + API_KEY)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {

            String s = response.body().string().replace(" ", "");
            int[] a = {23, 24, 25};
            String s1 = deleteAll(s, a);
            return s1;
        } else {
            throw new IOException("Unexcepted Code" + response);
        }
    }

    public String getCityInfo() throws IOException {

        Request request = new Request.Builder()
                .url(citylist)
                .build();

        Response response = client.newCall(request).execute();
        String city;
        if (response.isSuccessful()) {
            city = response.body().string();

        } else {
            throw new IOException("Unexcepted Code" + response);
        }
        return city;
    }


    public WeatherInfo json2weather(String json) {
        return new Gson().fromJson(json, WeatherInfo.class);
    }

    public CityInfo json2city(String json) {
        return new Gson().fromJson(json, CityInfo.class);
    }


    private String deleteAll(String source, int arg[]) {
        char[] array = source.toCharArray();
        String[] arrayStr = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            arrayStr[i] = String.valueOf(array[i]);
        }
        for (int key : arg) {
            arrayStr[key - 1] = "";
        }
        StringBuffer strBuf = new StringBuffer();
        for (String string : arrayStr) {
            if (!"".equals(string)) {
                strBuf.append(string);
            }
        }
        return strBuf.toString();
    }

}
