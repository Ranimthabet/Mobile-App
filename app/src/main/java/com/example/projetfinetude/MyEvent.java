package com.example.projetfinetude;

public class
MyEvent {
    String   event_username,event_userimage,time,event_title,event_location,event_start,event_discription;
    public MyEvent(){}

    public MyEvent(String event_username, String event_userimage, String time, String event_title, String event_location, String event_start, String event_discription) {
        this.event_username = event_username;
        this.event_userimage = event_userimage;
        this.time = time;
        this.event_title = event_title;
        this.event_location = event_location;
        this.event_start = event_start;
        this.event_discription = event_discription;
    }

    public String getEvent_username() {
        return event_username;
    }

    public void setEvent_username(String event_username) {
        this.event_username = event_username;
    }

    public String getEvent_userimage() {
        return event_userimage;
    }

    public void setEvent_userimage(String event_userimage) {
        this.event_userimage = event_userimage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_start() {
        return event_start;
    }

    public void setEvent_start(String event_start) {
        this.event_start = event_start;
    }

    public String getEvent_discription() {
        return event_discription;
    }

    public void setEvent_discription(String event_discription) {
        this.event_discription = event_discription;
    }
}
