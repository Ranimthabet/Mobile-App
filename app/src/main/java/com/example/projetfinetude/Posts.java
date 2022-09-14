package com.example.projetfinetude;

public class Posts {
    String userUId,username,postDiscription,timeAgo,imagePost,userprofileimage;

    public Posts(){}

    public Posts(String userUId,String username, String postDiscription, String timeAgo, String imagePost, String userprofileimage) {
        this.userUId = userUId;
        this.username = username;
        this.postDiscription = postDiscription;
        this.timeAgo = timeAgo;
        this.imagePost = imagePost;
        this.userprofileimage = userprofileimage;
    }

    public String getUserUId() {
        return userUId;
    }

    public void setUserUId(String userUId) {
        this.userUId = userUId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostDiscription() {
        return postDiscription;
    }

    public void setPostDiscription(String postDiscription) {
        this.postDiscription = postDiscription;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getImagePost() {
        return imagePost;
    }

    public void setImagePost(String imagePost) {
        this.imagePost = imagePost;
    }

    public String getUserprofileimage() {
        return userprofileimage;
    }

    public void setUserprofileimage(String userprofileimage) {
        this.userprofileimage = userprofileimage;
    }
}
