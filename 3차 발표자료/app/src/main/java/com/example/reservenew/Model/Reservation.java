package com.example.reservenew.Model;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Reservation {
    private String uid;
    private String room_id;
    private String type;
    private String room_number;
    private String total_price;
    private String check_in_time;
    private String check_out_time;
    private String check_in_date;
    private String check_out_date;
    private String add_user = "";
    QueryDocumentSnapshot documentSnapshot;

    public Reservation(){

    }

    public Reservation(String uid, String room_id, String type, String room_number, String total_price,
                       String check_in_time, String check_out_time, String check_in_date, String check_out_date){
        this.uid = uid;
        this.room_id = room_id;
        this.type = type;
        this.room_number = room_number;
        this.total_price = total_price;
        this.check_in_time = check_in_time;
        this.check_out_time = check_out_time;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
    }

    public void readSnapShot(QueryDocumentSnapshot document){
        this.documentSnapshot = document;
        this.uid              = document.getString("uid");
        this.room_id          = document.getString("room_id");
        this.type             = document.getString("type");
        this.room_number      = document.getString("room_number");
        this.total_price      = document.getString("total_price");
        this.check_in_time    = document.getString("check_in_time");
        this.check_out_time   = document.getString("check_out_time");
        this.check_in_date    = document.getString("check_in_date");
        this.check_out_date   = document.getString("check_out_date");
        this.add_user         = document.getString("add_user");
    }

    public Map<String,Object> to_Map(){
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("uid", uid);
        reservation.put("room_id", room_id);
        reservation.put("type", type);
        reservation.put("room_number", room_number);
        reservation.put("total_price", total_price);
        reservation.put("check_in_time", check_in_time);
        reservation.put("check_out_time", check_out_time);
        reservation.put("check_in_date", check_in_date);
        reservation.put("check_out_date", check_out_date);
        reservation.put("add_user", add_user);

        return reservation;
    }

    public void setAdd_user(String add_user) {
        this.add_user = add_user;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public void setCheck_in_time(String check_in_time) {
        this.check_in_time = check_in_time;
    }

    public void setCheck_out_time(String check_out_time) {
        this.check_out_time = check_out_time;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }

    public String getUid() {
        return uid;
    }

    public String getRoom_number() {
        return room_number;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public String getCheck_in_time() {
        return check_in_time;
    }

    public String getType() {
        return type;
    }

    public QueryDocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getAdd_user() {
        return add_user;
    }
}
