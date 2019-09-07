package com.example.chenguo.DataGet;

public class Parameters {
    private String size = "15";
    private String startDate = "";
    private String endDate = "";
    private String words = "";
    private String categories = "";

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    Parameters() {
    }

    Parameters(String size, String startDate, String endDate, String words, String categories) {
        this.size = size;
        this.startDate = startDate;
        this.endDate = endDate;
        this.words = words;
        this.categories = categories;
    }
}
