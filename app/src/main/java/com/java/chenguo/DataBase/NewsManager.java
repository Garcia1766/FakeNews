package com.java.chenguo.DataBase;

import android.util.Log;
import com.java.chenguo.DataGet.OneNews;
import com.java.chenguo.DataGet.OneNewsD;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用来对数据库进行操作的类。
 */
public class NewsManager {
    /*    private static NewsManager instance = null;
    public static NewsManager getInstance() {
        if (instance == null) {
            instance = new NewsManager();
            LitePal.deleteAll(OneNews.class);
            LitePal.deleteAll(ReadHistory.class);
            LitePal.deleteAll(FavHistory.class);
        }
        return instance;
    }*/

    private static final String TAG = "NewsManager";

    /**
     * 加入新闻至浏览记录
     * @param userID
     * @param newsID
     * @param oneNews
     */
    public static void addReadHistory(String userID, String newsID, OneNews oneNews) {
        List<ReadHistory> readHistories = LitePal.where("userID = ? and newsID = ?", userID, newsID).find(ReadHistory.class);
        if (readHistories.size() == 0) {
            ReadHistory his = new ReadHistory(userID, newsID);
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();// 获取当前时间
            his.setReadTime(sdf.format(date));
            his.save();
            OneNewsD oneNewsD = new OneNewsD(oneNews);
            //Log.d("test", "add url:" + oneNewsD.getImage());
            oneNewsD.save();
        } else {
            ReadHistory his = new ReadHistory(userID, newsID);
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();// 获取当前时间
            his.setReadTime(sdf.format(date));
            his.updateAll("userID = ? and newsID = ?", userID, newsID);
        }

        addUserKeywords(userID, oneNews.getKeywords());
    }

    /**
     * 加入新闻至收藏夹
     * @param userID
     * @param newsID
     * @param oneNews
     */
    public static void addFavHistory(String userID, String newsID, OneNews oneNews) {
        List<FavHistory> favHistories = LitePal.where("userID = ? and newsID = ?", userID, newsID).find(FavHistory.class);
        if (favHistories.size() == 0) {
            FavHistory his = new FavHistory(userID, newsID);
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();// 获取当前时间
            his.setReadTime(sdf.format(date));
            his.save();
            OneNewsD oneNewsD = new OneNewsD(oneNews);
            oneNewsD.save();
        } else {
            FavHistory his = new FavHistory(userID, newsID);
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();// 获取当前时间
            his.setReadTime(sdf.format(date));
            his.updateAll("userID = ? and newsID = ?", userID, newsID);
        }

        addUserKeywords(userID, oneNews.getKeywords());
    }

    /**
     * 删除单条浏览记录
     * @param userID
     * @param newsID
     */
    public static void deleteReadHistory(String userID, String newsID){
        LitePal.deleteAll(ReadHistory.class, "userID = ? and newsID = ?", userID, newsID);
    }

    /**
     * 删除单条收藏记录
     * @param userID
     * @param newsID
     */
    public static void deleteFavHistory(String userID, String newsID){
        LitePal.deleteAll(FavHistory.class, "userID = ? and newsID = ?", userID, newsID);
    }

    /**
     * 清空浏览记录
     * @param userID
     */
    public static void clearReadHistory(String userID) {
        LitePal.deleteAll(ReadHistory.class, "userID = ?", userID);
    }

    /**
     * 清空收藏夹
     * @param userID
     */
    public static void clearFavHistory(String userID) {
        LitePal.deleteAll(FavHistory.class, "userID = ?", userID);
    }

    /**
     * 显示浏览记录
     * @param userID
     * @return
     */
    public static ArrayList<OneNews> showReadHistory(String userID) {
        ArrayList<ReadHistory> readHistories =new ArrayList<>();
        List<ReadHistory> tmpList = LitePal.order("readTime desc").where("userID = ?", userID).find(ReadHistory.class);
        if(tmpList.size() > 0) {
            readHistories.addAll(tmpList);
        }
        ArrayList<OneNewsD> newsD = new ArrayList<>();
        ArrayList<OneNews> news = new ArrayList<>();
        for (ReadHistory history : readHistories) {
            OneNewsD oneNewsD = LitePal.where("newsID = ?", history.getNewsID()).findFirst(OneNewsD.class);
            //Log.d("test", "show url:" + oneNewsD.getImage());
            newsD.add(oneNewsD);
        }
        if(newsD.size() > 0) {
            for (OneNewsD oneNewsD : newsD) {
                news.add(new OneNews(oneNewsD));
            }
        }
        return news;
    }

    /**
     * 显示收藏夹
     * @param userID
     * @return
     */
    public static ArrayList<OneNews> showFavHistory(String userID) {
        ArrayList<FavHistory> favHistories =new ArrayList<>();
        List<FavHistory> tmpList = LitePal.order("readTime desc").where("userID = ?", userID).find(FavHistory.class);
        if(tmpList.size() > 0) {
            favHistories.addAll(tmpList);
        }

        ArrayList<OneNewsD> newsD = new ArrayList<>();
        ArrayList<OneNews> news = new ArrayList<>();
        for (FavHistory history : favHistories) {
            OneNewsD oneNewsD = LitePal.where("newsID = ?", history.getNewsID()).findFirst(OneNewsD.class);
            newsD.add(oneNewsD);
        }
        if(newsD.size() > 0) {
            for (OneNewsD oneNewsD : newsD) {
                news.add(new OneNews(oneNewsD));
            }
        }
        return news;
    }

    /**
     * 判断一个新闻是否在浏览记录中
     * @param newsID
     * @return
     */
    public static boolean searchReadHistory(String userID, String newsID){
        List<ReadHistory> his = LitePal.where("userID = ? and newsID = ?", userID, newsID).find(ReadHistory.class);
        return his.size() > 0;
    }

    /**
     * 判断一个新闻是否在收藏夹中
     * @param newsID
     * @return
     */
    public static boolean searchFavHistory(String userID, String newsID){
        List<FavHistory> his = LitePal.where("userID = ? and newsID = ?", userID, newsID).find(FavHistory.class);
        return his.size() > 0;
    }

    /**
     * 更新用户的频道
     * 传入当前用户ID、已选频道列表、备选频道列表，后两者都是String的ArrayList
     * @param userID
     * @param topMenuChoice
     * @param topMenuChoiceOthers
     */
    public static void updateTopMenu(String userID, ArrayList<String> topMenuChoice, ArrayList<String> topMenuChoiceOthers){
        LitePal.deleteAll(UserTopMenu.class, "userID = ?", userID);
        LitePal.deleteAll(UserTopMenuOthers.class, "userID = ?", userID);

        ArrayList<UserTopMenu> userTopMenuArrayList = new ArrayList<>();
        for(String it : topMenuChoice){
            userTopMenuArrayList.add(new UserTopMenu(userID, it));
        }
        for(UserTopMenu it : userTopMenuArrayList){
            it.save();
        }
        /*List<UserTopMenu> userTopMenuList = LitePal.where("userID = ?", userID).find(UserTopMenu.class);
        for(UserTopMenu it : userTopMenuList){
            Log.d(TAG, "topMenuChoice: " + it.getOneMenu());
        }

         */

        ArrayList<UserTopMenuOthers> userTopMenuOthersArrayList = new ArrayList<>();
        for(String it : topMenuChoiceOthers){
            userTopMenuOthersArrayList.add(new UserTopMenuOthers(userID, it));
        }
        for(UserTopMenuOthers it : userTopMenuOthersArrayList){
            it.save();
        }
        /*List<UserTopMenuOthers> userTopMenuOthersList = LitePal.where("userID = ?", userID).find(UserTopMenuOthers.class);
        for(UserTopMenuOthers it : userTopMenuOthersList){
            Log.d(TAG, "topMenuChoiceOthers: " + it.getOneMenu());
        }

         */
    }

    /**
     * 返回给定用户的已选频道列表，是个ArrayList<String>
     * @param userID
     * @return
     */
    public static ArrayList<String> getTopMenu (String userID){
        List<UserTopMenu> choice = LitePal.where("userID = ?", userID).find(UserTopMenu.class);
        ArrayList<String> topMenus = new ArrayList<>();
        for(UserTopMenu it : choice){
            topMenus.add(it.getOneMenu());
        }
        return topMenus;
    }

    /**
     * 返回给定用户的备选频道列表，是个ArrayList<String>
     * @param userID
     * @return
     */
    public static ArrayList<String> getTopMenuOthers (String userID){
        List<UserTopMenuOthers> choice = LitePal.where("userID = ?", userID).find(UserTopMenuOthers.class);
        ArrayList<String> topMenus = new ArrayList<>();
        for(UserTopMenuOthers it : choice){
            topMenus.add(it.getOneMenu());
        }
        return topMenus;
    }


    /**
     * 添加词句至搜索记录
     * @param userID
     * @param oneHistory
     */
    public static void addSearchHistory(String userID, String oneHistory) {
        SearchHistory his = new SearchHistory(userID, oneHistory);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();// 获取当前时间
        his.setSearchTime(sdf.format(date));
        List<SearchHistory> searchHistories = LitePal.where("userID = ? and oneHistory = ?", userID, oneHistory).find(SearchHistory.class);
        if (searchHistories.size() == 0) {
            his.save();
        } else {
            his.updateAll("userID = ? and oneHistory = ?", userID, oneHistory);
        }
    }

    /**
     * 删除单条搜索记录
     * @param userID
     * @param oneHistory
     */
    public static void deleteSearchHistory(String userID, String oneHistory){
        LitePal.deleteAll(SearchHistory.class, "userID = ? and oneHistory = ?", userID, oneHistory);
    }

    /**
     * 清空搜索记录
     * @param userID
     */
    public static void clearSearchHistory(String userID){
        LitePal.deleteAll(SearchHistory.class, "userID = ?", userID);
    }

    /**
     * 显示搜索记录
     * @param userID
     * @return
     */
    public static ArrayList<String> showSearchHistory(String userID){
        ArrayList<String> searchHistories =new ArrayList<>();
        List<SearchHistory> tmpList = LitePal.order("searchTime desc").where("userID = ?", userID).find(SearchHistory.class);
        for(SearchHistory his : tmpList){
            searchHistories.add(his.getOneHistory());
        }
        return searchHistories;
    }


    /**
     * 增加与用户关联的关键词
     * @param userID
     * @param keywords
     */
    public static void addUserKeywords(String userID, ArrayList<String> keywords){
        for (String keyword : keywords) {
            UserKeyword userKeyword = new UserKeyword();
            userKeyword.setUserID(userID);
            userKeyword.setKeyword(keyword);
            List<UserKeyword> tmpList = LitePal.where("userID = ? and keyword = ?", userID, keyword).find(UserKeyword.class);
            if(tmpList.size() == 0){
                userKeyword.setTimes(1);
                userKeyword.save();
            }else{
                userKeyword.setTimes(tmpList.get(0).getTimes() + 1);
                userKeyword.updateAll("userID = ? and keyword = ?", userID, keyword);
            }
        }
    }

    /**
     * 清空用户的关联关键词
     * @param userID
     */
    public static void clearUserKeywords(String userID){
        LitePal.deleteAll(UserKeyword.class, "userID = ?", userID);
    }

    /**
     * 得到与该用户相关度最高的k个关键词类，getKeyword获得关键词，getTimes获得次数
     * @param userID
     * @return
     */
    public static ArrayList<UserKeyword> getUserKeywordPairs(String userID, int num){
        List<UserKeyword> tmpList = LitePal.order("times desc").where("userID = ?", userID).find(UserKeyword.class);
        ArrayList<UserKeyword> userKeywordPairs = new ArrayList<>();
        for(int i = 0; i < num && i < tmpList.size(); i++){
            userKeywordPairs.add(tmpList.get(i));
        }
        return userKeywordPairs;
    }

    /**
     * 增加与用户关联的不喜欢关键词
     * @param userID
     * @param keywords
     */
    public static void addUserDKKeywords(String userID, ArrayList<String> keywords){
        //Log.d("test", "add, username:" + userID);
        for (String keyword : keywords) {
            UserDKKeyword userKeyword = new UserDKKeyword();
            userKeyword.setUserID(userID);
            userKeyword.setKeyword(keyword);
            List<UserDKKeyword> tmpList = LitePal.where("userID = ? and dkkeyword = ?", userID, keyword).find(UserDKKeyword.class);
            if(tmpList.size() == 0){
                userKeyword.setTimes(1);
                userKeyword.save();
            }else{
                userKeyword.setTimes(tmpList.get(0).getTimes() + 1);
                userKeyword.updateAll("userID = ? and dkkeyword = ?", userID, keyword);
            }
        }
    }

    /**
     * 清空用户的不喜欢关联关键词
     * @param userID
     */
    public static void clearUserDKKeywords(String userID){
        //Log.d("test", "触发清除dk");
        LitePal.deleteAll(UserDKKeyword.class, "userID = ?", userID);
    }

    /**
     * 得到与该用户相关度最高的k个不喜欢关键词类，getKeyword获得关键词，getTimes获得次数
     * @param userID
     * @return
     */
    public static ArrayList<UserDKKeyword> getUserDKKeywordPairs(String userID, int num){
        List<UserDKKeyword> tmpList = LitePal.order("times desc").where("userID = ?", userID).find(UserDKKeyword.class);
        ArrayList<UserDKKeyword> userKeywordPairs = new ArrayList<>();
        for(int i = 0; i < num && i < tmpList.size(); i++){
            userKeywordPairs.add(tmpList.get(i));
        }
        return userKeywordPairs;
    }

    public static void addUser(User user){
        user.save();
    }

}
