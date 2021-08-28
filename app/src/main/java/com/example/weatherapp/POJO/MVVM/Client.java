package com.example.weatherapp.POJO.MVVM;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit weatherRetrofit;
    private static Retrofit geocoderRetrofit;

    public static synchronized Retrofit getWeatherInstance() {
        if (weatherRetrofit == null) {
            String BASE_URL = "https://api.ambeedata.com/weather/";
            weatherRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return weatherRetrofit;
    }

    public static synchronized Retrofit getGeocoderInstance(){
        if (geocoderRetrofit == null) {
            String BASE_URL = "http://api.positionstack.com/v1/";
            geocoderRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return geocoderRetrofit;
    }

}
