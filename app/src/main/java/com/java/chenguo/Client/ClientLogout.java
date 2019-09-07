package com.java.chenguo.Client;

import android.util.Log;

import com.java.chenguo.DataBase.FavHistory;
import com.java.chenguo.DataGet.OneNewsD;
import com.java.chenguo.DataBase.ReadHistory;
import com.java.chenguo.DataBase.SearchHistory;
import com.java.chenguo.DataBase.UserDKKeyword;
import com.java.chenguo.DataBase.UserKeyword;
import com.java.chenguo.DataBase.UserTopMenu;
import com.java.chenguo.DataBase.UserTopMenuOthers;
import com.java.chenguo.DataBase.User;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 与ClientLogin类结构相似。仅含一个静态方法，用户登出时调用这个方法
 */
public class ClientLogout {
    private static final String TAG = "ClientLogout";

    /**
     * 点击“登出”键时，调用这个函数。每次登出时，把本地数据库同步至服务器
     * @param userID 即将退出的用户名
     */
    public static void sendLogoutRequest(String userID){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                List<User> userList = LitePal.findAll(User.class);
                List< SearchHistory > searchHistoryList = LitePal.findAll(SearchHistory.class);
                List< ReadHistory > readHistoryList = LitePal.findAll(ReadHistory.class);
                List< FavHistory > favHistoryList = LitePal.findAll(FavHistory.class);
                List< UserKeyword > userKeywordList = LitePal.findAll(UserKeyword.class);
                List< UserDKKeyword > userDKKeywordList = LitePal.findAll(UserDKKeyword.class);
                List< UserTopMenu > userTopMenuList = LitePal.findAll(UserTopMenu.class);
                List< UserTopMenuOthers > userTopMenuOthersList = LitePal.findAll(UserTopMenuOthers.class);
                List< OneNewsD > oneNewsDList = LitePal.findAll(OneNewsD.class);
                UploadData uploadData = new UploadData(userList, searchHistoryList, readHistoryList, favHistoryList, userKeywordList,
                        userDKKeywordList, userTopMenuList, userTopMenuOthersList, oneNewsDList);
                Log.d(TAG, "Local Database: " + gson.toJson(uploadData));
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("uploadData", gson.toJson(uploadData))
                        .build();
                Request request = new Request.Builder()
                        .url("http://183.172.195.28:8000/logout/")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    // Log.d(TAG, "response get: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
