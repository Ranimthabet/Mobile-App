package com.example.projetfinetude;

public class MyNote {
    String useruid,destination;
Integer note;
    public MyNote() {
    }

    public MyNote(String useruid, String destination, Integer note) {
        this.useruid = useruid;
        this.destination = destination;
        this.note = note;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }
}
