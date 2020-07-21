package com.example.fbuapp.Models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Location")
public class Location extends ParseObject {

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_GEO_POINT = "geoPoint";

    public Location(){
    }

    public Number getLatitude(){
        return getNumber(KEY_LATITUDE);
    }

    public void setLatitude(Number latitude){
        put(KEY_LATITUDE, latitude);
    }

    public Number getLongitude(){
        return getNumber(KEY_LONGITUDE);
    }

    public void setLongitude(Number longitude){
        put(KEY_LONGITUDE, longitude);
    }

    public String getCity(){
        return getString(KEY_CITY);
    }

    public void setCity(String city){
        put(KEY_CITY, city);
    }

    public String getState(){
        return getString(KEY_STATE);
    }

    public void setState(String state){
        put(KEY_STATE, state);
    }

    public ParseGeoPoint getGeoPoint(){
        return getParseGeoPoint(KEY_GEO_POINT);
    }

    public void setGeoPoint(ParseGeoPoint geoPoint){
        put(KEY_GEO_POINT, geoPoint);
    }
}
