package com.java.chenguo.DataBase;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private String userID;
    private String password;

    public User() {
    }

    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
