package com.example.projetfinetude;

public class MyGroup {
    String group_id,groupe_image,groupe_name,groupe_information ,group_creator;

    public MyGroup() {
    }

    public MyGroup(String group_id, String groupe_image, String groupe_name, String groupe_information, String group_creator) {
        this.group_id = group_id;
        this.groupe_image = groupe_image;
        this.groupe_name = groupe_name;
        this.groupe_information = groupe_information;
        this.group_creator = group_creator;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroupe_image() {
        return groupe_image;
    }

    public void setGroupe_image(String groupe_image) {
        this.groupe_image = groupe_image;
    }

    public String getGroupe_name() {
        return groupe_name;
    }

    public void setGroupe_name(String groupe_name) {
        this.groupe_name = groupe_name;
    }

    public String getGroupe_information() {
        return groupe_information;
    }

    public void setGroupe_information(String groupe_information) {
        this.groupe_information = groupe_information;
    }

    public String getGroup_creator() {
        return group_creator;
    }

    public void setGroup_creator(String group_creator) {
        this.group_creator = group_creator;
    }
}
