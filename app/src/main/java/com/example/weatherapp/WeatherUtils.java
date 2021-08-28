package com.example.weatherapp;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.weatherapp.Classes.Forecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherUtils {


    @SuppressLint("SimpleDateFormat")
    public static Date convertUnixToUTC(long unixSeconds) {
        Date date = new java.util.Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+2"));
        String formattedDate = sdf.format(date);
        String[] arr = formattedDate.split(",");
        return new Date(Integer.parseInt(arr[0]) - 1900, Integer.parseInt(arr[1]) - 1, Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]));
    }

    public static int fahrenheitToCelsius(double deg) {
        return (int) ((deg - 32) * 5 / 9);
    }

    public static String getDateFormat(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());
        return df.format(date);
    }

    public static List<Forecast> getTodaysWeather(List<Forecast> forecast) {
        List<Forecast> tmp = new ArrayList<>();
        Date date = Calendar.getInstance().getTime();
        for (int i = 0; i < forecast.size(); i++) {

            if (WeatherUtils.getDateFormat(date).equals(WeatherUtils.getDateFormat(WeatherUtils.convertUnixToUTC(forecast.get(i).getTime())))) {
                tmp.add(forecast.get(i));
            }
        }
        return tmp;
    }

    public static int chooseWeatherIcon(String iconName) {
        if (iconName.contains("day")) {
            if (iconName.contains("clear"))
                return R.drawable.sun;
            else
                return R.drawable.cloudy_sun;
        } else if (iconName.contains("night")) {
            if (iconName.contains("clear"))
                return R.drawable.moon;
            else
                return R.drawable.cloudy_moon;
        } else if (iconName.contains("rain") || iconName.contains("snow")) {
            return R.drawable.rains;
        }
        return R.drawable.clouds;
    }

    public static Forecast getNowForecast(List<Forecast> forecasts, Date date) {
        for (int i = 1; i < forecasts.size(); i++) {
            if (date.getTime() < WeatherUtils.convertUnixToUTC(forecasts.get(i).getTime()).getTime())
                return forecasts.get(i - 1);
        }
        return forecasts.get(forecasts.size() - 1);
    }

    public static int chooseColorBasedOnTime(float hour) {

        if (hour < 12) {
            hour = 12 - hour;
        } else {
            hour -= 12;
        }
        hour *= 8;
        hour /= 100;
        int start = 0x00b0ff, end = 0x05001b;
        StringBuilder color = new StringBuilder(new StringBuilder(Integer.toHexString((Integer) new ArgbEvaluator().evaluate(hour, start, end))));
        while (color.length() < 6) {
            color.insert(0, '0');
        }
        return Color.parseColor("#" + color);
    }


    public static long getFirstUpdateTime(){
        long curr = WeatherUtils.convertUnixToUTC(Calendar.getInstance().getTime().getTime()).getTime();
        long updateTime = ((curr - (curr % 3600000)) + 3600000) - curr;
        return updateTime;
    }

    public static long getUpdateTime(Context context) {
        boolean first = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("first",true);
        if(first){
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("first",false).apply();
            return getFirstUpdateTime();
        }
        else{
            return 3600000;
        }
    }
}
