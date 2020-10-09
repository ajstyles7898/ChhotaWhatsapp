package com.example.chhotawhatsapp.UserActivity;

public class UserItem {

    private String name;  //all three needs to be same named as in database
    private String about;
    private String image;

    public UserItem(String name, String about, String image) {
        this.name = name;
        this.about = about;
        this.image = image;
    }

    public UserItem() {
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
