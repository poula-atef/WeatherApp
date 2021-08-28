package com.example.weatherapp.POJO.Classes;

import java.util.List;

public class Data{
    private double lat;
    private double lng;
    private List<Forecast> forecast;

    public Data() {
    }

    public Data(double lat, double lng, List<Forecast> forecast) {
        this.lat = lat;
        this.lng = lng;
        this.forecast = forecast;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }
}
