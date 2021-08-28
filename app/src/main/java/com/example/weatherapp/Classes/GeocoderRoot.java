package com.example.weatherapp.Classes;

import java.util.List;

public class GeocoderRoot {
    private List<Datum> data;

    public GeocoderRoot() {
    }

    public GeocoderRoot(List<Datum> data) {
        this.data = data;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}
