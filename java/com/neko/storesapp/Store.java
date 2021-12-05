package com.neko.storesapp;

public class Store {

    private String id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private int photo;

    public Store () {

    }

    public Store(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Store(String id, String name, String description, double latitude, double longitude, int photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
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
}
