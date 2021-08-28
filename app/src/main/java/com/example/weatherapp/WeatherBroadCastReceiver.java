package com.example.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.weatherapp.Room.WeatherDatabase;

import java.util.Observable;

public class WeatherBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WeatherService weatherService = new WeatherService();
        weatherService.handleWidgetUpdate(context);
    }
}
