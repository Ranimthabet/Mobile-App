package com.example.projetfinetude;

public class Comment {
    String userUid,userName,profilImageUri,Comment;
public Comment (){}

    public Comment(String userUid, String userName, String profilImageUri, String comment) {
        this.userUid = userUid;
        this.userName = userName;
        this.profilImageUri = profilImageUri;
        Comment = comment;
    }
    public String getUserUid() {
        return userUid;
    }
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getProfilImageUri() {
        return profilImageUri;
    }
    public void setProfilImageUri(String profilImageUri) {
        this.profilImageUri = profilImageUri;
    }
    public String getComment() {
        return Comment;
    }
    public void setComment(String comment) {
        Comment = comment;
    }
}
