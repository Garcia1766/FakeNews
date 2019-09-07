package com.example.chenguo.DataBase;

/**
 * 浏览记录。记录不是完整的新闻，因此仅有三个特征userID, newID和readTime（浏览时间）
 */
public class ReadHistory extends LocalHistory {
    private String userID;
    private String newsID;
    private String readTime;

    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    public void setUserID(String userID) { this.userID = userID; }

    @Override
    public String getNewsID() { return newsID; }

    @Override
    public void setNewsID(String newsID) { this.newsID = newsID; }

    @Override
    public String getReadTime() { return readTime; }

    @Override
    public void setReadTime(String readTime) { this.readTime = readTime; }

    public ReadHistory(){}

    public ReadHistory(String userID, String newsID) {
        this.userID = userID;
        this.newsID = newsID;
    }

    public ReadHistory(String userID, String newsID, String readTime) {
        this.userID = userID;
        this.newsID = newsID;
        this.readTime = readTime;
    }
}
