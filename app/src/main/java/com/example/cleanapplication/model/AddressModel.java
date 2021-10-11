package com.example.cleanapplication.model;

public class AddressModel {

    String address;
    String addressID;
    String userID;
    String userLat;
    String userLong;
    String createdAt, updatedAt;

    public AddressModel(String address, String addressID, String userID) {
        this.address = address;
        this.addressID = addressID;
        this.userID = userID;
    }

    public AddressModel(String address, String addressID, String userID, String userLat, String userLong) {
        this.address = address;
        this.addressID = addressID;
        this.userID = userID;
        this.userLat = userLat;
        this.userLong = userLong;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLong() {
        return userLong;
    }

    public void setUserLong(String userLong) {
        this.userLong = userLong;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
