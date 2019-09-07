package com.example.chenguo.DataBase;

import org.litepal.crud.LitePalSupport;

/**
 * 浏览记录或收藏记录，被ReadHistory和FavHistory继承
 * 记录不是完整的新闻，因此只用存三个特征：userID标明是哪个用户，newsID用来找新闻，readTime用来在显示的时候排序
 */
public abstract class LocalHistory extends LitePalSupport {

    abstract public String getUserID();

    abstract public void setUserID(String userID);

    abstract public String getNewsID();

    abstract public void setNewsID(String newsID);

    abstract public String getReadTime();

    abstract public void setReadTime(String readTime);
}
