package com.example.weatherapp.Classes;

public class ForecastRoot {
    private String message;
    private Data data;

    public ForecastRoot() {
    }

    public ForecastRoot(String message, Data data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}