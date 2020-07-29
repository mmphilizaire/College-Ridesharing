package com.example.fbuapp.Models;

import java.util.Date;

public class RideRequestFilter {

    private boolean hasStartLocation;
    private Location startLocation;
    private int startMileRadius;

    private boolean hasEndLocation;
    private Location endLocation;
    private int endMileRadius;

    private boolean hasDeparture;
    private Date departure;

    public RideRequestFilter(){

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

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
        this.hasDeparture = true;
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
