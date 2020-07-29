package com.example.fbuapp.Models;

import java.util.Date;

public class RideOfferFilter {

    private boolean hasStartLocation;
    private Location startLocation;
    private int startMileRadius;

    private boolean hasEndLocation;
    private Location endLocation;
    private int endMileRadius;

    private boolean hasEarliestDeparture;
    private Date earliestDeparture;

    private boolean hasLatestDeparture;
    private Date latestDeparture;

    private boolean hideFullRides;

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
