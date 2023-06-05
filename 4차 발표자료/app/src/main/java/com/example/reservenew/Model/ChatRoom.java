package com.example.reservenew.Model;

public class ChatRoom {
    private String user_name;
    private String recent_msg;
    private String uid;

    public ChatRoom(String user_name,String uid, String recent_msg){
        this.user_name = user_name;
        this.uid = uid;
        this.recent_msg = recent_msg;
    }

    public String getUid() {
        return uid;
    }

    public String getRecent_msg() {
        return recent_msg;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRecent_msg(String recent_msg) {
        this.recent_msg = recent_msg;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
