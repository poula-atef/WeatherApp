package com.example.weatherapp.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.weatherapp.Classes.Forecast;

@Database(entities = {Forecast.class},version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    private static WeatherDatabase instance;

    public abstract WeatherDao weatherDao();

    public static synchronized WeatherDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,WeatherDatabase.class,"weather_db")
                    .build();
        }
        return instance;
    }

}
