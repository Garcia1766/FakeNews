package com.java.chenguo.DataBase;

import org.litepal.crud.LitePalSupport;

public class UserDKKeyword extends LitePalSupport {
    private String userID;
    private String dkkeyword;
    private int times;

    public UserDKKeyword() {
    }

    public UserDKKeyword(String userID, String dkkeyword, int times) {
        this.userID = userID;
        this.dkkeyword = dkkeyword;
        this.times = times;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getKeyword() {
        return dkkeyword;
    }

    public void setKeyword(String dkkeyword) {
        this.dkkeyword = dkkeyword;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void incTimes(){
        this.times++;
    }
}
