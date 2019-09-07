package com.example.chenguo.DataBase;

import org.litepal.crud.LitePalSupport;

public class UserKeyword extends LitePalSupport {
    private String userID;
    private String keyword;
    private int times;

    public UserKeyword() {
    }

    public UserKeyword(String userID, String keyword, int times) {
        this.userID = userID;
        this.keyword = keyword;
        this.times = times;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
