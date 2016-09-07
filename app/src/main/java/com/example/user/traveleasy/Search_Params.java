package com.example.user.traveleasy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 21-08-2016.
 */
public class Search_Params  {

 private String origin;
    private String destination;
    private String date;
    private int adults;
    private int childrens;
    private  String cabin_class;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildrens() {
        return childrens;
    }

    public void setChildrens(int childrens) {
        this.childrens = childrens;
    }

    public String getCabin_class() {
        return cabin_class;
    }

    public void setCabin_class(String cabin_class) {
        this.cabin_class = cabin_class;
    }


}
