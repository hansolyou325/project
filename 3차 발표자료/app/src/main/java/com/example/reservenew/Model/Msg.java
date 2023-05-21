package com.example.reservenew.Model;

public class Msg {
    private String user;
    private String msg;
    private String time;
    private String uid;


    public Msg(){

    }

    public Msg(String user, String msg,String time){
        this.user = user;
        this.msg = msg;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
