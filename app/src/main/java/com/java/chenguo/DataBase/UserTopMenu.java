package com.java.chenguo.DataBase;

import org.litepal.crud.LitePalSupport;

public class UserTopMenu extends LitePalSupport {
    private String userID;
    private String oneMenu;

    public UserTopMenu(String userID, String oneMenu) {
        this.userID = userID;
        this.oneMenu = oneMenu;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOneMenu() {
        return oneMenu;
    }

    public void setOneMenu(String oneMenu) {
        this.oneMenu = oneMenu;
    }
}
