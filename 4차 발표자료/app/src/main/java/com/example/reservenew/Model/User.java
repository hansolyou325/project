package com.example.reservenew.Model;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String email;
    private String name;
    private String phone;
    private String grade;
    private String auth_code;
    private QueryDocumentSnapshot documentSnapshot;

    public User(){

    }

    public User(String uid, String email, String name, String phone, String grade, String auth_code){
        this.uid    = uid;
        this.email  = email;
        this.name   = name;
        this.phone  = phone;
        this.grade  = grade;
        this.auth_code = auth_code;
    }

    public void readSnapShot(QueryDocumentSnapshot document){
        this.documentSnapshot = document;
        this.uid            = document.getString("id");
        this.email          = document.getString("email");
        this.name           = document.getString("name");
        this.phone          = document.getString("phone");
        this.grade          = document.getString("grade");
        this.auth_code      = document.getString("auth_code");
    }

    public Map<String,Object> to_Map(){
        Map<String, Object> user = new HashMap<>();
        user.put("id", uid);
        user.put("email", email);
        user.put("name", name);
        user.put("phone", phone);
        user.put("grade", grade);
        user.put("auth_code", auth_code);

        return user;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public String getGrade() {
        return grade;
    }

    public QueryDocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }
}
