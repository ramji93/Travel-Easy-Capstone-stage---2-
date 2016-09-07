package com.example.user.traveleasy;

import java.util.ArrayList;

/**
 * Created by user on 13-08-2016.
 */
public class Itenary {

    private String LegId;
    private int Origin;
    private int ParentOrg;
    private int Destination;
    private int ParentDest;

    private String Departure;
    private String Arrival;
    private int Duration;
    private int Stops;
    private ArrayList<Integer> carriers;

    private String Booking_Uri;
    private String Booking_Body;

    public int getParentOrg() {
        return ParentOrg;
    }

    public void setParentOrg(int parentOrg) {
        ParentOrg = parentOrg;
    }

    public int getParentDest() {
        return ParentDest;
    }

    public void setParentDest(int parentDest) {
        ParentDest = parentDest;
    }

    public String getLegId() {
        return LegId;
    }

    public void setLegId(String legId) {
        LegId = legId;
    }

    public int getOrigin() {
        return Origin;
    }

    public void setOrigin(int origin) {
        Origin = origin;
    }

    public int getDestination() {
        return Destination;
    }

    public void setDestination(int destination) {
        Destination = destination;
    }

    public String getDeparture() {
        return Departure;
    }

    public void setDeparture(String departure) {
        Departure = departure;
    }

    public String getArrival() {
        return Arrival;
    }

    public void setArrival(String arrival) {
        Arrival = arrival;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public int getStops() {
        return Stops;
    }

    public void setStops(int stops) {
        Stops = stops;
    }

    public ArrayList<Integer> getCarriers() {
        return carriers;
    }

    public void setCarriers(ArrayList<Integer> carriers) {
        this.carriers = carriers;
    }

    public String getBooking_Uri() {
        return Booking_Uri;
    }

    public void setBooking_Uri(String booking_Uri) {
        Booking_Uri = booking_Uri;
    }

    public String getBooking_Body() {
        return Booking_Body;
    }

    public void setBooking_Body(String booking_Body) {
        Booking_Body = booking_Body;
    }
}
