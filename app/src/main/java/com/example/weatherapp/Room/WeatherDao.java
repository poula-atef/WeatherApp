package com.example.weatherapp.Room;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.weatherapp.Classes.Forecast;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = REPLACE)
    void insertWeather(List<Forecast> weather);

    @Query("select * from forecast")
    List<Forecast> getAllWeather();

    @Query("delete from forecast")
    void removeAllWeather();

}
