package com.example.chenguo.DataGet;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.example.chenguo.DataBase.NewsManager;
import com.example.chenguo.DataBase.UserDKKeyword;
import com.example.chenguo.DataBase.UserKeyword;
import com.example.chenguo.NewsList_Adapter;
import com.example.chenguo.DataGet.OneNews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.lang.Runnable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GrabData extends AppCompatActivity {

    private static final String TAG = "GrabData";

    public ArrayList<OneNews> returnNews = new ArrayList<>();

    public void sendRequestWithOkHttp(String size, String startDate, String endDate, String words, String categories, final Activity activity, final NewsList_Adapter adapter, boolean flag, boolean flag_hot, String username) {

        final Parameters para = new Parameters(size, startDate, endDate, words, categories);

        ArrayList<UserDKKeyword> dk_data = NewsManager.getUserDKKeywordPairs(username, 3);
        ArrayList<String> dk_str = new ArrayList<>();
        for(UserDKKeyword dk : dk_data) {
            dk_str.add(dk.getKeyword());
        }


        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean add_it = true;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://api2.newsminer.net/svc/news/queryNewsList?size="+para.getSize()
                                    +"&startDate="+para.getStartDate()
                                    +"&endDate="+para.getEndDate()
                                    +"&words="+para.getWords()
                                    +"&categories="+para.getCategories())
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    try{
                        JSONObject jsonObject = new JSONObject(responseData);
                        String data = jsonObject.getString("data");
                        JSONArray jsonArray = new JSONArray(data);
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject oneNews = jsonArray.getJSONObject(i);
                            String title = oneNews.getString("title");
                            String publisher = oneNews.getString("publisher");
                            String publishTime = oneNews.getString("publishTime");
                            String content = oneNews.getString("content");
                            String url = oneNews.getString("url");
                            String newsID = oneNews.getString("newsID");
                            String category = oneNews.getString("category");
                            String imageArray = oneNews.getString("image");
                            imageArray = imageArray.replaceAll("\\[", "");
                            imageArray = imageArray.replaceAll("\\]", "");
                            ArrayList<String> image = new ArrayList<>(Arrays.asList(imageArray.split(", ")));
                            if(image.size() == 1 && image.get(0).equals("")) {
                                image.clear();
                            }
                            String video = oneNews.getString("video");
                            JSONArray keywordsArray = oneNews.getJSONArray("keywords");
                            ArrayList<String> keywords = new ArrayList<>();
                            for(int j=0; j<keywordsArray.length(); j++){
                                JSONObject oneKeyword = keywordsArray.getJSONObject(j);
                                keywords.add(oneKeyword.getString("word"));
                            }
                            OneNews oneNews1 = new OneNews(title, publisher, publishTime, content, url,
                                    newsID, category, image, video, keywords);

                            Random rand = new Random();
                            for(int j = 0; j < dk_str.size(); j++) {
                                if(keywords.contains(dk_str.get(j))) {
                                    int randNum = rand.nextInt(10);
                                    if (randNum < 8 - j) {
                                        add_it = false;
                                        break;
                                    }
                                }
                            }

                            if(add_it) {
                                returnNews.add(oneNews1);
                            }
                            add_it = true;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                if(flag_hot) {
                    synchronized (returnNews) {
                        Set<OneNews> unique = new TreeSet<>((o1, o2) -> o1.getNewsID().compareTo(o2.getNewsID()));
                        unique.addAll(returnNews);
                        returnNews.clear();
                        for(OneNews on : unique){
                            returnNews.add(on);
                        }
                        Collections.sort(returnNews, Comparator.comparing(OneNews::getPublishTime));
                        Collections.reverse(returnNews);
                    }
                }
                //Log.d("test", "抓取完成");

                if(adapter != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Log.d("test", "刷新中");
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        thread.start();
        if(!flag) {
            try {
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
