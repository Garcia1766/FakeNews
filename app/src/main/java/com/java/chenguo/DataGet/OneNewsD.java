package com.java.chenguo.DataGet;

import com.java.chenguo.DataGet.OneNews;

import org.litepal.crud.LitePalSupport;

public class OneNewsD extends LitePalSupport {
    private String title = "";
    private String publisher = "";
    private String publishTime = "";
    private String content = "";
    private String url = "";
    private String newsID = "";
    private String category = "";
    private String image = "";
    private String video = "";
    private String keywords = "";

    public OneNewsD(String title, String publisher, String publishTime, String content, String url, String newsID, String category, String image, String video, String keywords) {
        this.title = title;
        this.publisher = publisher;
        this.publishTime = publishTime;
        this.content = content;
        this.url = url;
        this.newsID = newsID;
        this.category = category;
        this.image = image;
        this.video = video;
        this.keywords = keywords;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getPublisher() { return publisher; }

    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getPublishTime() { return publishTime; }

    public void setPublishTime(String publishTime) { this.publishTime = publishTime; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNewsID() { return newsID; }

    public void setNewsID(String newsID) { this.newsID = newsID; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getVideo() { return video; }

    public void setVideo(String video) { this.video = video; }

    public String getKeywords() { return keywords; }

    public void setKeywords(String keywords) { this.keywords = keywords; }

    public OneNewsD() { }

    public OneNewsD(OneNews oneNews){
        this.title = oneNews.getTitle();
        this.publisher = oneNews.getPublisher();
        this.publishTime = oneNews.getPublishTime();
        this.content = oneNews.getContent();
        this.url = oneNews.getUrl();
        this.newsID = oneNews.getNewsID();
        this.category = oneNews.getCategory();
        for(String str : oneNews.getImage()){
            this.image = this.image + str + ",";
        }
        if(this.image.length() > 0){
            this.image = this.image.substring(0, this.image.length()-1);
        }

        this.video = oneNews.getVideo();

        for(String str : oneNews.getKeywords()){
            this.keywords = this.keywords + str + ",";
        }
        if(this.keywords.length() > 0){
            this.keywords = this.keywords.substring(0, this.keywords.length()-1);
        }
    }
}
