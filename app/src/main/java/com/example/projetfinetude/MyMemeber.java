package com.example.projetfinetude;

public class MyMemeber {
    String member_name,member_email,member_uri;

    public MyMemeber() {
    }

    public MyMemeber(String member_name, String member_email, String member_uri) {
        this.member_name = member_name;
        this.member_email = member_email;
        this.member_uri = member_uri;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getMember_uri() {
        return member_uri;
    }

    public void setMember_uri(String member_uri) {
        this.member_uri = member_uri;
    }
}
