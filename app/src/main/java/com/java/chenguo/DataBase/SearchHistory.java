package com.java.chenguo.DataBase;

import org.litepal.crud.LitePalSupport;

public class SearchHistory extends LitePalSupport {
    private String userID;
    private String oneHistory;
    private String searchTime;

    public SearchHistory() { }

    public SearchHistory(String userID, String oneHistory) {
        this.userID = userID;
        this.oneHistory = oneHistory;
    }

    public SearchHistory(String userID, String oneHistory, String searchTime) {
        this.userID = userID;
        this.oneHistory = oneHistory;
        this.searchTime = searchTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOneHistory() {
        return oneHistory;
    }

    public void setOneHistory(String oneHistory) {
        this.oneHistory = oneHistory;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }
}
