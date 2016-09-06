package com.example.reol.hdweather.Utils;

import android.widget.TextView;

import com.example.reol.hdweather.Entity.WeatherInfo;

/**为了简化访问天气信息强行加上的类
 * Created by reol on 16-9-4.
 */
public class WeatherUtils {

    public String getCityName(WeatherInfo info) {
        return info.getHeWeatherdataservice().get(0).getBasic().getCity();
    }

    public String getNowTmp(WeatherInfo info) {
        return info.getHeWeatherdataservice().get(0).getNow().getTmp();
    }

    public String getPM25(WeatherInfo info){
        return info.getHeWeatherdataservice().get(0).getAqi().getCity().getPm25();
    }

    public String getInfo(WeatherInfo info, int num, int key) {
        String result = "fail";
        switch (key) {
            case 1:result = info.getHeWeatherdataservice().get(0).getDaily_forecast().get(num).getDate();
                break;
            case 2:result = info.getHeWeatherdataservice().get(0).getDaily_forecast().get(num).getTmp().getMin();
                break;
            case 3:result = info.getHeWeatherdataservice().get(0).getDaily_forecast().get(num).getTmp().getMax();
                break;
            case 4:result = info.getHeWeatherdataservice().get(0).getDaily_forecast().get(num).getCond().getTxt_d();
                break;
            case 5:result = info.getHeWeatherdataservice().get(0).getDaily_forecast().get(num).getCond().getTxt_n();
                break;
            case 6:result = info.getHeWeatherdataservice().get(0).getDaily_forecast().get(num).getPop();
                break;
        }
        return result;
    }
}
