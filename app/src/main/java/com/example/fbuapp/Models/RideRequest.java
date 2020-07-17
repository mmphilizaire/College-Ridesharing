package com.example.fbuapp.Models;

import android.text.format.DateFormat;

import com.example.fbuapp.Models.Location;
import com.example.fbuapp.Models.RideOffer;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;

@Parcel(analyze = RideOffer.class)
@ParseClassName("RideRequest")
public class RideRequest extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_EARLIEST_DEPARTURE = "earliestDeparture";
    public static final String KEY_LATEST_DEPARTURE = "latestDeparture";
    public static final String KEY_START_LOCATION = "startLocation";
    public static final String KEY_END_LOCATION = "endLocation";

    public RideRequest(){

    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public Date getEarliestDeparture(){
        return getDate(KEY_EARLIEST_DEPARTURE);
    }

    public void setEarliestDeparture(Date earliestDeparture){
        put(KEY_EARLIEST_DEPARTURE, earliestDeparture);
    }

    public Date getLatestDeparture(){
        return getDate(KEY_LATEST_DEPARTURE);
    }

    public void setLatestDeparture(Date latestDeparture){
        put(KEY_LATEST_DEPARTURE, latestDeparture);
    }

    public Location getStartLocation(){
        return (Location) get(KEY_START_LOCATION);
    }

    public void setStartLocation(Location startLocation){
        put(KEY_START_LOCATION, startLocation);
    }

    public Location getEndLocation(){
        return (Location) get(KEY_END_LOCATION);
    }

    public void setEndLocation(Location endLocation){
        put(KEY_END_LOCATION, endLocation);
    }

    public String getDay(Date date){
        return (String) DateFormat.format("EEEE", date);
    }

    public String getDateWithYear(Date date){
        return (String) DateFormat.format("MMMM d, yyyy", date);
    }

    public String getDateNoYear(Date date){
        return (String) DateFormat.format("MMMM d", date);
    }

    public String getTime(Date date){
        return (String) DateFormat.format("h:mm", date);
    }

}
