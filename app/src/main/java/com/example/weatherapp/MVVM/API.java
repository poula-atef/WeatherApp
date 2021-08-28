package com.example.weatherapp.MVVM;

import com.example.weatherapp.Classes.ForecastRoot;
import com.example.weatherapp.Classes.GeocoderRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface API {
    String AMBEE_API_KEY = "c87397fa7d9f9910927e8a33eb3638ab4c7283b0f63fac0d214b2660d11648dc";
    String POSITIONSTACK_API_KEY = "c26d34615bad371f39ff155e01924540";

    @Headers({"x-api-key: " + AMBEE_API_KEY,"Content-type: application/json"})
    @GET("forecast/by-lat-lng")
    Call<ForecastRoot> getHourlyData(@Query("lat") double lat, @Query("lng") double lng, @Query("filter") String filter);

    @GET("reverse?access_key=" + POSITIONSTACK_API_KEY)
    Call<GeocoderRoot> getGeocoderData(@Query("query")String query);

}
