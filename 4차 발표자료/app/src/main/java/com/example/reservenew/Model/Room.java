package com.example.reservenew.Model;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String id;
    private String hotel_id;
    private String type;
    private String is_available;
    private String room_number;
    private String check_in_time;
    private String check_out_time;
    private String capacity;
    private String heaad_count;
    QueryDocumentSnapshot documentSnapshot;

    public void readSnapShot(QueryDocumentSnapshot document){
        this.documentSnapshot = document;
        this.id               = document.getString("id");
        this.hotel_id         = document.getString("hotel_id");
        this.type             = document.getString("type");
        this.is_available     = document.getString("is_available");
        this.room_number      = document.getString("room_number");
        this.check_in_time    = document.getString("check_in_time");
        this.check_out_time   = document.getString("check_out_time");
        this.capacity         = document.getString("capacity");
        this.heaad_count      = document.getString("head_count");
    }

    public Map<String,Object> to_Map(){
        Map<String, Object> room = new HashMap<>();
        room.put("id", id);
        room.put("hotel_id", hotel_id);
        room.put("is_available", is_available);
        room.put("room_number", room_number);
        room.put("check_in_time", check_in_time);
        room.put("check_out_time", check_out_time);
        room.put("capacity", capacity);
        room.put("head_count", heaad_count);

        return room;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public void setCheck_in_time(String check_in_time) {
        this.check_in_time = check_in_time;
    }

    public void setCheck_out_time(String check_out_time) {
        this.check_out_time = check_out_time;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setHeaad_count(String heaad_count) {
        this.heaad_count = heaad_count;
    }

    public String getId() {
        return id;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getType() {
        return type;
    }

    public String getCheck_in_time() {
        return check_in_time;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public String getHeaad_count() {
        return heaad_count;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public String getIs_available() {
        return is_available;
    }

    public String getRoom_number() {
        return room_number;
    }

    public QueryDocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }
}
