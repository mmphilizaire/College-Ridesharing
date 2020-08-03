package com.example.fbuapp.Models;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class RideOfferFilter {

     boolean hasStartLocation;
     Location startLocation;
     int startMileRadius;

     boolean hasEndLocation;
     Location endLocation;
     int endMileRadius;

     boolean hasEarliestDeparture;
     Date earliestDeparture;

     boolean hasLatestDeparture;
     Date latestDeparture;

     boolean hideFullRides;

    public RideOfferFilter(){

    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
        this.hasStartLocation = true;
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
        this.hasEndLocation = true;
    }

    public int getEndMileRadius() {
        return endMileRadius;
    }

    public void setEndMileRadius(int endMileRadius) {
        this.endMileRadius = endMileRadius;
    }

    public Date getEarliestDeparture() {
        return earliestDeparture;
    }

    public void setEarliestDeparture(Date earliestDeparture) {
        this.earliestDeparture = earliestDeparture;
        this.hasEarliestDeparture = true;
    }

    public Date getLatestDeparture() {
        return latestDeparture;
    }

    public void setLatestDeparture(Date latestDeparture) {
        this.latestDeparture = latestDeparture;
        this.hasLatestDeparture = true;
    }

    public boolean getHideFullRides() {
        return hideFullRides;
    }

    public void setHideFullRides(boolean hideFullRides) {
        this.hideFullRides = hideFullRides;
    }

    public boolean hasStartLocation() {
        return hasStartLocation;
    }

    public boolean hasEndLocation() {
        return hasEndLocation;
    }

    public boolean hasEarliestDeparture() {
        return hasEarliestDeparture;
    }

    public boolean hasLatestDeparture() {
        return hasLatestDeparture;
    }

}
