package com.example.user.traveleasy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 15-08-2016.
 */
public class Segment{

    private int id;
    private int origin;
    private int destination;
    private String departure;
    private String arrival;
    private int flightno;
    private int carrier;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public int getFlightno() {
        return flightno;
    }

    public void setFlightno(int flightno) {
        this.flightno = flightno;
    }

    public int getCarrier() {
        return carrier;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }



}

