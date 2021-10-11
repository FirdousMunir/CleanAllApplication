package com.example.cleanapplication.model;

public class SlotsModel {

    String DayOfWeek , DateOfWeek, availableTime , pickupID;

    public SlotsModel(String dayOfWeek, String dateOfWeek, String availableTime, String pickupID) {
        DayOfWeek = dayOfWeek;
        DateOfWeek = dateOfWeek;
        this.availableTime = availableTime;
        this.pickupID = pickupID;
    }

    public String getPickupID() {
        return pickupID;
    }

    public void setPickupID(String pickupID) {
        this.pickupID = pickupID;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public String getDateOfWeek() {
        return DateOfWeek;
    }

    public void setDateOfWeek(String dateOfWeek) {
        DateOfWeek = dateOfWeek;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }
}

