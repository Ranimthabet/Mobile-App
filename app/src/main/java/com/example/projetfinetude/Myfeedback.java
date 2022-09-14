package com.example.projetfinetude;

public class Myfeedback {
    String uid,username,userimage,time,feedback;
    public  Myfeedback(){}

    public Myfeedback(String uid, String username, String userimage, String time, String feedback) {
        this.uid = uid;
        this.username = username;
        this.userimage = userimage;
        this.time = time;
        this.feedback = feedback;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
