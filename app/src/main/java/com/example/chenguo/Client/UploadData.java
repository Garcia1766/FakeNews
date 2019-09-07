package com.example.chenguo.Client;

import com.example.chenguo.DataBase.FavHistory;
import com.example.chenguo.DataGet.OneNewsD;
import com.example.chenguo.DataBase.ReadHistory;
import com.example.chenguo.DataBase.SearchHistory;
import com.example.chenguo.DataBase.UserDKKeyword;
import com.example.chenguo.DataBase.UserKeyword;
import com.example.chenguo.DataBase.UserTopMenu;
import com.example.chenguo.DataBase.UserTopMenuOthers;
import com.example.chenguo.DataBase.User;

import java.util.ArrayList;
import java.util.List;

public class UploadData {
    private List<User> userList = new ArrayList<>();
    private List<SearchHistory> searchHistoryList = new ArrayList<>();
    private List<ReadHistory> readHistoryList = new ArrayList<>();
    private List<FavHistory> favHistoryList = new ArrayList<>();
    private List<UserKeyword> userKeywordList = new ArrayList<>();
    private List<UserDKKeyword> userDKKeywordList = new ArrayList<>();
    private List<UserTopMenu> userTopMenuList = new ArrayList<>();
    private List<UserTopMenuOthers> userTopMenuOthersList = new ArrayList<>();
    private List<OneNewsD> oneNewsDList = new ArrayList<>();

    public UploadData() { }

    public UploadData(List<User> userList, List<SearchHistory> searchHistoryList, List<ReadHistory> readHistoryList, List<FavHistory> favHistoryList, List<UserKeyword> userKeywordList, List<UserDKKeyword> userDKKeywordList, List<UserTopMenu> userTopMenuList, List<UserTopMenuOthers> userTopMenuOthersList, List<OneNewsD> oneNewsDList) {
        this.userList = userList;
        this.searchHistoryList = searchHistoryList;
        this.readHistoryList = readHistoryList;
        this.favHistoryList = favHistoryList;
        this.userKeywordList = userKeywordList;
        this.userDKKeywordList = userDKKeywordList;
        this.userTopMenuList = userTopMenuList;
        this.userTopMenuOthersList = userTopMenuOthersList;
        this.oneNewsDList = oneNewsDList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<SearchHistory> getSearchHistoryList() {
        return searchHistoryList;
    }

    public void setSearchHistoryList(List<SearchHistory> searchHistoryList) {
        this.searchHistoryList = searchHistoryList;
    }

    public List<ReadHistory> getReadHistoryList() {
        return readHistoryList;
    }

    public void setReadHistoryList(List<ReadHistory> readHistoryList) {
        this.readHistoryList = readHistoryList;
    }

    public List<FavHistory> getFavHistoryList() {
        return favHistoryList;
    }

    public void setFavHistoryList(List<FavHistory> favHistoryList) {
        this.favHistoryList = favHistoryList;
    }

    public List<UserKeyword> getUserKeywordList() {
        return userKeywordList;
    }

    public void setUserKeywordList(List<UserKeyword> userKeywordList) {
        this.userKeywordList = userKeywordList;
    }

    public List<UserDKKeyword> getUserDKKeywordList() {
        return userDKKeywordList;
    }

    public void setUserDKKeywordList(List<UserDKKeyword> userDKKeywordList) {
        this.userDKKeywordList = userDKKeywordList;
    }

    public List<UserTopMenu> getUserTopMenuList() {
        return userTopMenuList;
    }

    public void setUserTopMenuList(List<UserTopMenu> userTopMenuList) {
        this.userTopMenuList = userTopMenuList;
    }

    public List<UserTopMenuOthers> getUserTopMenuOthersList() {
        return userTopMenuOthersList;
    }

    public void setUserTopMenuOthersList(List<UserTopMenuOthers> userTopMenuOthersList) {
        this.userTopMenuOthersList = userTopMenuOthersList;
    }

    public List<OneNewsD> getOneNewsDList() {
        return oneNewsDList;
    }

    public void setOneNewsDList(List<OneNewsD> oneNewsDList) {
        this.oneNewsDList = oneNewsDList;
    }
}
