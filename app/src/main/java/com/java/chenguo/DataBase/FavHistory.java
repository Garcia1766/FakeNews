package com.java.chenguo.DataBase;

/**
 * 收藏记录，即收藏夹内容。记录不是完整的新闻，因此仅有三个特征userID, newID和readTime（收藏时间）
 */
public class FavHistory extends LocalHistory {
    private String userID;
    private String newsID;
    private String readTime;

    @Override
    public String getUserID() { return userID; }

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

    public FavHistory(){};

    public FavHistory(String userID, String newsID) {
        this.userID = userID;
        this.newsID = newsID;
    }

    public FavHistory(String userID, String newsID, String readTime){
        this.userID = userID;
        this.newsID = newsID;
        this.readTime = readTime;
    }
}
