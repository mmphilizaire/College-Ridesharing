package com.example.fbuapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Parcel(analyze = RideOffer.class)
@ParseClassName("RideOffer")
public class RideOffer extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_DEPARTURE_TIME = "departureTime";
    public static final String KEY_START_LOCATION = "startLocation";
    public static final String KEY_END_LOCATION = "endLocation";
    public static final String KEY_SEAT_PRICE = "seatPrice";
    public static final String KEY_SEAT_COUNT = "seatCount";
    public static final String KEY_PASSENGERS = "passengers";

    public RideOffer(){
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public Date getDepartureTime(){
        return getDate(KEY_DEPARTURE_TIME);
    }

    public void setDepartureTime(Date departureTime){
        put(KEY_DEPARTURE_TIME, departureTime);
    }

    public String getStartLocation(){
        return getString(KEY_START_LOCATION);
    }

    public void setStartLocation(String startLocation){
        put(KEY_START_LOCATION, startLocation);
    }

    public String getEndLocation(){
        return getString(KEY_END_LOCATION);
    }

    public void setEndLocation(String endLocation){
        put(KEY_END_LOCATION, endLocation);
    }

    public Number getSeatPrice(){
        return getNumber(KEY_SEAT_PRICE);
    }

    public void setSeatPrice(Number seatPrice){
        put(KEY_SEAT_PRICE, seatPrice);
    }

    public Number getSeatCount(){
        return getNumber(KEY_SEAT_COUNT);
    }

    public void setSeatCount(Number seatCount){
        put(KEY_SEAT_COUNT, seatCount);
    }

    public JSONArray getPassengers(){
        return getJSONArray(KEY_PASSENGERS);
    }

    public void setPassengers(JSONArray passengers){
        put(KEY_PASSENGERS, passengers);
    }

    public String getDay(){
        Date date = getDepartureTime();
        return (String) DateFormat.format("EEEE", date);
    }

    public String getDateWithYear(){
        Date date = getDepartureTime();
        return (String) DateFormat.format("MMMM d, yyyy", date);
    }

    public String getDateNoYear(){
        Date date = getDepartureTime();
        return (String) DateFormat.format("MMMM d", date);
    }

    public String getTime(){
        Date date = getDepartureTime();
        return (String) DateFormat.format("h:mm", date);
    }

}
