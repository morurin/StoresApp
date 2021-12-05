package com.neko.storesapp;

import androidx.lifecycle.ViewModel;

public class CustomViewModel extends ViewModel {

    private String storeViewId;
    private double latitude;
    private double longitude;
    private String name;
    private String description;
    private String username;
    private String userEmail;
    private String userDescription;
    

    public  CustomViewModel(){

    }


    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreViewId() {
        return storeViewId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setStoreViewId(String storeViewId) {
        this.storeViewId = storeViewId;
    }
}


