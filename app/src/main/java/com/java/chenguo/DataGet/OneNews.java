package com.java.chenguo.DataGet;

import android.util.Log;

import com.java.chenguo.DataGet.OneNewsD;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Arrays;

public class OneNews {
    private String title = "";
    private String publisher = "";
    private String publishTime = "";
    private String content = "";
    private String url = "";
    private String newsID = "";
    private String category = "";
    private ArrayList<String> image = new ArrayList<>();
    private String video = "";
    private ArrayList<String> keywords = new ArrayList<>();

    public ArrayList<String> getKeywords() { return keywords; }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getImage() { return image; }

    public void setImage(ArrayList<String> image) { this.image = image; }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public OneNews(){ }

    public OneNews(String title, String publisher, String publishTime, String content, String url, String newsID,
                   String category, ArrayList<String> image, String video){
        this.title = title;
        this.publisher = publisher;
        this.publishTime = publishTime;
        this.content = content;
        this.url = url;
        this.newsID = newsID;
        this.category = category;
        this.image = image;
        this.video = video;
    }

    public OneNews(String title, String publisher, String publishTime, String content, String url, String newsID,
                   String category, ArrayList<String> image, String video, ArrayList<String> keywords){
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

    public OneNews(OneNewsD oneNewsD){
        this.title = oneNewsD.getTitle();
        this.publisher = oneNewsD.getPublisher();
        this.publishTime = oneNewsD.getPublishTime();
        this.content = oneNewsD.getContent();
        this.url = oneNewsD.getUrl();
        this.newsID = oneNewsD.getNewsID();
        this.category = oneNewsD.getCategory();
        this.image.addAll(Arrays.asList(oneNewsD.getImage().split(",")));
        this.video = oneNewsD.getVideo();
        this.keywords.addAll(Arrays.asList(oneNewsD.getKeywords().split(",")));
    }
}
