package com.example.weatherapp.POJO.Classes;

public class Datum {
    private double latitude;
    private double longitude;
    private String type;
    private double distance;
    private String name;
    private String number;
    private String postal_code;
    private String street;
    private double confidence;
    private String region;
    private String region_code;
    private String county;
    private String locality;
    private Object administrative_area;
    private Object neighbourhood;
    private String country;
    private String country_code;
    private String continent;
    private String label;

    public Datum() {
    }

    public Datum(double latitude, double longitude, String type, double distance, String name, String number, String postal_code, String street, double confidence, String region, String region_code, String county, String locality, Object administrative_area, Object neighbourhood, String country, String country_code, String continent, String label) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.distance = distance;
        this.name = name;
        this.number = number;
        this.postal_code = postal_code;
        this.street = street;
        this.confidence = confidence;
        this.region = region;
        this.region_code = region_code;
        this.county = county;
        this.locality = locality;
        this.administrative_area = administrative_area;
        this.neighbourhood = neighbourhood;
        this.country = country;
        this.country_code = country_code;
        this.continent = continent;
        this.label = label;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Object getAdministrative_area() {
        return administrative_area;
    }

    public void setAdministrative_area(Object administrative_area) {
        this.administrative_area = administrative_area;
    }

    public Object getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(Object neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
