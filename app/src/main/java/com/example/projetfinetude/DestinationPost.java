package com.example.projetfinetude;

public class DestinationPost {
    String useruid,username,userimage,time,posturi;

    public DestinationPost() {
    }

    public DestinationPost(String useruid, String username, String userimage, String time, String posturi) {
        this.useruid = useruid;
        this.username = username;
        this.userimage = userimage;
        this.time = time;
        this.posturi = posturi;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
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

    public String getPosturi() {
        return posturi;
    }

    public void setPosturi(String posturi) {
        this.posturi = posturi;
    }
}
