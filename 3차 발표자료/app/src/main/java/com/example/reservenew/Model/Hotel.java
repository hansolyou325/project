package com.example.reservenew.Model;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Hotel {
    private String id;
    private String type;
    private String bed;
    private String price_per_night;
    private String info;
    private String capacity;
    private String image_url;
    QueryDocumentSnapshot documentSnapshot;

    public void readSnapShot(QueryDocumentSnapshot document){
        this.documentSnapshot = document;
        this.id                 = document.getString("id");
        this.type               = document.getString("type");
        this.bed                = document.getString("bed");
        this.price_per_night    = document.getString("price_per_night");
        this.info               = document.getString("info");
        this.capacity           = document.getString("capacity");
        this.image_url          = document.getString("image_url");
    }

    public Map<String,Object> to_Map(){
        Map<String, Object> hotel = new HashMap<>();
        hotel.put("id", id);
        hotel.put("type", type);
        hotel.put("bed", bed);
        hotel.put("price_per_night", price_per_night);
        hotel.put("info", info);
        hotel.put("capacity", capacity);
        hotel.put("image_url", image_url);

        return hotel;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public void setPrice_per_night(String price_per_night) {
        this.price_per_night = price_per_night;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getBed() {
        return bed;
    }

    public String getPrice_per_night() {
        return price_per_night;
    }

    public String getInfo() {
        return info;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getImage_url() {
        return image_url;
    }
}
