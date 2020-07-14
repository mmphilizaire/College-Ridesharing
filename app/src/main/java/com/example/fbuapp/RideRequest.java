package com.example.fbuapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze = RideOffer.class)
@ParseClassName("RideRequest")
public class RideRequest extends ParseObject {
    public static final String KEY_USER = "user";

    public RideRequest(){

    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }


}
