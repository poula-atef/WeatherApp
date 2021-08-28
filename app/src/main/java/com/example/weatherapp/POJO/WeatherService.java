package com.example.weatherapp.POJO;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.weatherapp.POJO.Classes.Forecast;
import com.example.weatherapp.POJO.Room.WeatherDatabase;
import com.example.weatherapp.POJO.Utils.WeatherUtils;
import com.example.weatherapp.UI.WeatherWidget;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherService extends IntentService {

    public WeatherService(){
        super("WeatherService");
    }

    public WeatherService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    public void handleWidgetUpdate(Context context){
        WeatherDatabase db = WeatherDatabase.getInstance(context);
        Observable.create(new ObservableOnSubscribe<List<Forecast>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Forecast>> emitter) throws Throwable {
                emitter.onNext(db.weatherDao().getAllWeather());
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Forecast>>() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void accept(List<Forecast> forecasts) throws Throwable {
                List<Forecast> tmp = WeatherUtils.getTodaysWeather(forecasts);
                Forecast currentForecast = WeatherUtils.getNowForecast(tmp, Calendar.getInstance().getTime());

                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
                WeatherWidget.updateAppWidgets(context,manager,appWidgetIds,currentForecast);
            }
        });
    }

}
