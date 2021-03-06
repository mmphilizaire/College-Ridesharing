package com.example.fbuapp.Models;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class RideRequestFilter {

     boolean hasStartLocation;
     Location startLocation;
     int startMileRadius;

     boolean hasEndLocation;
     Location endLocation;
     int endMileRadius;

     boolean hasDeparture;
     Date departure;

    public RideRequestFilter(){

    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
        if(startLocation == null){
            this.hasStartLocation = false;
        }
        else{
            this.hasStartLocation = true;
        }
    }

    public int getStartMileRadius() {
        return startMileRadius;
    }

    public void setStartMileRadius(int startMileRadius) {
        this.startMileRadius = startMileRadius;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
        if(endLocation == null){
            this.hasEndLocation = false;
        }
        else{
            this.hasEndLocation = true;
        }
    }

    public int getEndMileRadius() {
        return endMileRadius;
    }

    public void setEndMileRadius(int endMileRadius) {
        this.endMileRadius = endMileRadius;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
        if(departure == null){
            this.hasDeparture = false;
        }
        else{
            this.hasDeparture = true;
        }
    }

    public boolean hasStartLocation() {
        return hasStartLocation;
    }

    public boolean hasEndLocation() {
        return hasEndLocation;
    }

    public boolean hasDeparture() {
        return hasDeparture;
    }

}
