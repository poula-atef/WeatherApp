package com.example.weatherapp.POJO;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WeatherBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WeatherService weatherService = new WeatherService();
        weatherService.handleWidgetUpdate(context);
    }
}
