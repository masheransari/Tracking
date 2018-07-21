package com.example.android.careemapp.ModelClass;

/**
 * Created by asher.ansari on 1/29/2018.
 */

public class CoordinateModel  {
    private String lat;
    private String lon;

    public String getLat() {
        return lat;
    }

    public CoordinateModel() {
    }

    public String getLon() {
        return lon;
    }

    public CoordinateModel(String lat, String lon) {

        this.lat = lat;
        this.lon = lon;
    }
}
