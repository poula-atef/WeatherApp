package com.example.weatherapp.UI;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import com.example.weatherapp.POJO.Classes.Forecast;
import com.example.weatherapp.R;
import com.example.weatherapp.POJO.WeatherBroadCastReceiver;
import com.example.weatherapp.POJO.Utils.WeatherUtils;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        long updateTime = WeatherUtils.getUpdateTime(context);
        if(updateTime > 0) {
            Intent intent = new Intent(context, WeatherBroadCastReceiver.class);
            PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntentAlarm);
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    updateTime,
                    pendingIntentAlarm);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.S)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Forecast forecast) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        views.setInt(R.id.widget_container,"setBackgroundColor", WeatherUtils.chooseColorBasedOnTime(WeatherUtils.convertUnixToUTC(forecast.getTime()).getHours()));

        views.setImageViewResource(R.id.icon, WeatherUtils.chooseWeatherIcon(forecast.getIcon()));

        views.setTextViewText(R.id.weather, WeatherUtils.fahrenheitToCelsius(forecast.getTemperature()) + "??");

        views.setTextViewText(R.id.time, WeatherUtils.getDateFormat(WeatherUtils.convertUnixToUTC(forecast.getTime())));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Forecast forecast) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, forecast);
        }

    }
}