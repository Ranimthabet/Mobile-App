package com.example.projetfinetude;

public class Friends {
private  String username,profileImageUri,email;
public Friends(){}

    public Friends(String username, String profileImageUri, String userUid,String email) {
        this.username = username;
        this.profileImageUri = profileImageUri;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
