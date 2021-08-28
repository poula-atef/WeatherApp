package com.example.weatherapp.MVVM;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.Classes.ForecastRoot;
import com.example.weatherapp.Classes.GeocoderRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<ForecastRoot> weatherLiveData;
    private MutableLiveData<GeocoderRoot> geocoderLiveData;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        geocoderLiveData = new MutableLiveData<>();
        weatherLiveData = new MutableLiveData<>();
    }



    private void getWeatherDataPrivate(double lat, double lng, String filter) {
        API api = Client.getWeatherInstance().create(API.class);

        Call<ForecastRoot> hourly = api.getHourlyData(lat, lng, filter);
        hourly.enqueue(new Callback<ForecastRoot>() {
            @Override
            public void onResponse(Call<ForecastRoot> call, Response<ForecastRoot> response) {
                weatherLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ForecastRoot> call, Throwable t) {

            }
        });
    }

    public void getWeatherData(double lat, double lng, String filter) {
        getWeatherDataPrivate(lat, lng, filter);
    }

    public MutableLiveData<ForecastRoot> getWeatherLiveData() {
        return weatherLiveData;
    }


    private void getGeocoderDataPrivate(String query){
      API api = Client.getGeocoderInstance().create(API.class);
      Call<GeocoderRoot>response = api.getGeocoderData(query);
      response.enqueue(new Callback<GeocoderRoot>() {
          @Override
          public void onResponse(Call<GeocoderRoot> call, Response<GeocoderRoot> response) {
              geocoderLiveData.setValue(response.body());
          }

          @Override
          public void onFailure(Call<GeocoderRoot> call, Throwable t) {

          }
      });
    }

    public void getGeocoderData(String query){
        getGeocoderDataPrivate(query);
    }

    public MutableLiveData<GeocoderRoot> getGeocoderLiveData() {
        return geocoderLiveData;
    }



}

