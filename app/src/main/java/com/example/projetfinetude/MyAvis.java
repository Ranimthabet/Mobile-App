package com.example.projetfinetude;

public class MyAvis {
    String avis_useruid,avis_userimage,avis_username,avis_time,avis_text;
    public MyAvis(){}

    public MyAvis(String avis_useruid, String avis_userimage, String avis_username, String avis_time, String avis_text) {
        this.avis_useruid = avis_useruid;
        this.avis_userimage = avis_userimage;
        this.avis_username = avis_username;
        this.avis_time = avis_time;
        this.avis_text = avis_text;
    }

    public String getAvis_useruid() {
        return avis_useruid;
    }

    public void setAvis_useruid(String avis_useruid) {
        this.avis_useruid = avis_useruid;
    }

    public String getAvis_userimage() {
        return avis_userimage;
    }

    public void setAvis_userimage(String avis_userimage) {
        this.avis_userimage = avis_userimage;
    }

    public String getAvis_username() {
        return avis_username;
    }

    public void setAvis_username(String avis_username) {
        this.avis_username = avis_username;
    }

    public String getAvis_time() {
        return avis_time;
    }

    public void setAvis_time(String avis_time) {
        this.avis_time = avis_time;
    }

    public String getAvis_text() {
        return avis_text;
    }

    public void setAvis_text(String avis_text) {
        this.avis_text = avis_text;
    }
}
