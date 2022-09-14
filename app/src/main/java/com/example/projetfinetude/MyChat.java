package com.example.projetfinetude;

public class MyChat {
    private String message,status,userUId;

    public MyChat() {
    }

    public MyChat(String message, String status, String userUId) {
        this.message = message;
        this.status = status;
        this.userUId = userUId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserUId() {
        return userUId;
    }

    public void setUserUId(String userUId) {
        this.userUId = userUId;
    }
}
