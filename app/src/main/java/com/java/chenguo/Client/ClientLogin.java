package com.java.chenguo.Client;

import android.util.Log;

import com.java.chenguo.DataBase.FavHistory;
import com.java.chenguo.DataBase.NewsManager;
import com.java.chenguo.DataGet.OneNewsD;
import com.java.chenguo.DataBase.ReadHistory;
import com.java.chenguo.DataBase.SearchHistory;
import com.java.chenguo.DataBase.UserDKKeyword;
import com.java.chenguo.DataBase.UserKeyword;
import com.java.chenguo.DataBase.UserTopMenu;
import com.java.chenguo.DataBase.UserTopMenuOthers;
import com.java.chenguo.DataBase.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClientLogin {
    private static final String TAG = "ClientLogin";

    /**
     * “0”表示注册用户已存在，“1”表示注册新用户成功
     * “2”表示登录成功，“3”表示登录的用户不存在，“4”表示密码错误
     * 仅有“1”和“2”的情况需要对本地数据库进行处理
     */
    public static String responseType;

    /**
     * 登录页面，点击“注册”或“登录”
     * @param requestType “0”表示注册，“1”表示登录
     * @param userID 用户名
     * @param password 密码
     */
    public static void sendLoginRequest(final String requestType, final String userID, final String password){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("requestType", requestType)
                            .add("userID", userID)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://183.172.195.28:8000/login/")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "Internet Database: ");
                    Log.d(TAG, responseData);

                    try{
                        JSONObject jsonObject = new JSONObject(responseData);
                        responseType = jsonObject.getString("responseType");
                        if(responseType.equals("1")) {
                            NewsManager.addUser(new User(userID, password));
                            Log.d(TAG, "New User added: " + userID);
                        }
                        else if(responseType.equals("2")){
                            LitePal.deleteAll(User.class);
                            LitePal.deleteAll(SearchHistory.class);
                            LitePal.deleteAll(ReadHistory.class);
                            LitePal.deleteAll(FavHistory.class);
                            LitePal.deleteAll(UserKeyword.class);
                            LitePal.deleteAll(UserDKKeyword.class);
                            LitePal.deleteAll(UserTopMenu.class);
                            LitePal.deleteAll(UserTopMenuOthers.class);
                            LitePal.deleteAll(OneNewsD.class);

                            JSONArray userArray = jsonObject.getJSONArray("User");
                            JSONArray searchHistoryArray = jsonObject.getJSONArray("SearchHistory");
                            JSONArray readHistoryArray = jsonObject.getJSONArray("ReadHistory");
                            JSONArray favHistoryArray = jsonObject.getJSONArray("FavHistory");
                            JSONArray userKeywordArray = jsonObject.getJSONArray("UserKeyword");
                            JSONArray userDKKeywordArray = jsonObject.getJSONArray("UserDKKeyword");
                            JSONArray userTopMenuArray = jsonObject.getJSONArray("UserTopMenu");
                            JSONArray userTopMenuOthersArray = jsonObject.getJSONArray("UserTopMenuOthers");
                            JSONArray oneNewsDArray = jsonObject.getJSONArray("OneNewsD");
                            for(int i = 0; i < userArray.length(); i++) {
                                JSONObject jsonObject1 = userArray.getJSONObject(i);
                                User user = new User(jsonObject1.getString("userID"), jsonObject1.getString("password"));
                                user.save();
                            }
                            for(int i = 0; i < searchHistoryArray.length(); i++) {
                                JSONObject jsonObject1 = searchHistoryArray.getJSONObject(i);
                                SearchHistory searchHistory = new SearchHistory(jsonObject1.getString("userID"),
                                        jsonObject1.getString("oneHistory"), jsonObject1.getString("searchTime"));
                                searchHistory.save();
                            }
                            for(int i = 0; i < readHistoryArray.length(); i++) {
                                JSONObject jsonObject1 = readHistoryArray.getJSONObject(i);
                                ReadHistory readHistory = new ReadHistory(jsonObject1.getString("userID"),
                                        jsonObject1.getString("newsID"), jsonObject1.getString("readTime"));
                                readHistory.save();
                            }
                            for(int i = 0; i < favHistoryArray.length(); i++) {
                                JSONObject jsonObject1 = favHistoryArray.getJSONObject(i);
                                FavHistory favHistory = new FavHistory(jsonObject1.getString("userID"),
                                        jsonObject1.getString("newsID"), jsonObject1.getString("readTime"));
                                favHistory.save();
                            }
                            for(int i = 0; i < userKeywordArray.length(); i++) {
                                JSONObject jsonObject1 = userKeywordArray.getJSONObject(i);
                                UserKeyword userKeyword = new UserKeyword(jsonObject1.getString("userID"),
                                        jsonObject1.getString("keyword"), jsonObject1.getInt("times"));
                                userKeyword.save();
                            }
                            for(int i = 0; i < userDKKeywordArray.length(); i++) {
                                JSONObject jsonObject1 = userDKKeywordArray.getJSONObject(i);
                                UserDKKeyword userDKKeyword = new UserDKKeyword(jsonObject1.getString("userID"),
                                        jsonObject1.getString("dkkeyword"), jsonObject1.getInt("times"));
                                userDKKeyword.save();
                            }
                            for(int i = 0; i < userTopMenuArray.length(); i++) {
                                JSONObject jsonObject1 = userTopMenuArray.getJSONObject(i);
                                UserTopMenu userTopMenu = new UserTopMenu(jsonObject1.getString("userID"),
                                        jsonObject1.getString("oneMenu"));
                                userTopMenu.save();
                            }
                            for(int i = 0; i < userTopMenuOthersArray.length(); i++) {
                                JSONObject jsonObject1 = userTopMenuOthersArray.getJSONObject(i);
                                UserTopMenuOthers userTopMenuOthers = new UserTopMenuOthers(jsonObject1.getString("userID"),
                                        jsonObject1.getString("oneMenu"));
                                userTopMenuOthers.save();
                            }
                            for(int i = 0; i < oneNewsDArray.length(); i++) {
                                JSONObject jsonObject1 = oneNewsDArray.getJSONObject(i);
                                OneNewsD oneNewsD = new OneNewsD(jsonObject1.getString("title"),
                                        jsonObject1.getString("publisher"), jsonObject1.getString("publishTime"),
                                        jsonObject1.getString("content"), jsonObject1.getString("url"),
                                        jsonObject1.getString("newsID"), jsonObject1.getString("category"),
                                        jsonObject1.getString("image"), jsonObject1.getString("video"),
                                        jsonObject1.getString("keywords"));
                                oneNewsD.save();
                            }
                            Log.d(TAG, "Login Successfully");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}