package com.example.projetfinetude;

public class MyRandonnee {


    String randonne_username,randonne_userimage,time,randonne_title,randonne_location,randonne_start,randonne_discription;
    public MyRandonnee(){}

    public MyRandonnee(String randonne_username, String randonne_userimage, String time, String randonne_title, String randonne_location, String randonne_start, String randonne_discription) {
        this.randonne_username = randonne_username;
        this.randonne_userimage = randonne_userimage;
        this.time = time;
        this.randonne_title = randonne_title;
        this.randonne_location = randonne_location;
        this.randonne_start = randonne_start;
        this.randonne_discription = randonne_discription;
    }

    public String getRandonne_username() {
        return randonne_username;
    }

    public void setRandonne_username(String randonne_username) {
        this.randonne_username = randonne_username;
    }

    public String getRandonne_userimage() {
        return randonne_userimage;
    }

    public void setRandonne_userimage(String randonne_userimage) {
        this.randonne_userimage = randonne_userimage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRandonne_title() {
        return randonne_title;
    }

    public void setRandonne_title(String randonne_title) {
        this.randonne_title = randonne_title;
    }

    public String getRandonne_location() {
        return randonne_location;
    }

    public void setRandonne_location(String randonne_location) {
        this.randonne_location = randonne_location;
    }

    public String getRandonne_start() {
        return randonne_start;
    }

    public void setRandonne_start(String randonne_start) {
        this.randonne_start = randonne_start;
    }

    public String getRandonne_discription() {
        return randonne_discription;
    }

    public void setRandonne_discription(String randonne_discription) {
        this.randonne_discription = randonne_discription;
    }
}

