package com.abdallahomer12.naturereport;


public class EarthquakeInfo {
    private double magnitude;

    public EarthquakeInfo(String location, double magnitude, Long date, String uri) {
        this.location = location;
        this.magnitude = magnitude;
        this.date = date;
        this.uri = uri;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public Long getDate() {
        return date;
    }

    public String getUri() {
        return uri;
    }

    private String location;
    private Long date;
    private String uri;
}
