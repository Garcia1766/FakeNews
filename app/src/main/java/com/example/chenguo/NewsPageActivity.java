package com.example.chenguo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chenguo.DataBase.NewsManager;
import com.example.chenguo.DataGet.OneNews;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cn.jzvd.JzvdStd;

public class NewsPageActivity extends AppCompatActivity {

    TextView news_page_title;
    TextView news_page_author;
    TextView news_page_time;
    TextView news_page_text;
    Toolbar toolbar;

    ImageLoader imageLoader;

    String title;
    String time;
    String author;
    String text;
    String url;
    ArrayList<String> image;
    ArrayList<Bitmap> image_bitmap = new ArrayList<>();
    String video;
    ArrayList<String> keywords;
    String newsID;
    String category;
    String username;

    boolean isStored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        IWXAPI api;
        api  = WXAPIFactory.createWXAPI(this, getResources().getString(R.string.weixin_id), true);
        api.registerApp(getResources().getString(R.string.weixin_id));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        toolbar = (Toolbar) findViewById(R.id.news_page_tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        }
        actionBar.setTitle("洋芋新闻"); //前端代码移植到后端
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);


        news_page_title = (TextView) findViewById(R.id.news_page_title);
        news_page_author = (TextView) findViewById(R.id.news_page_author);
        news_page_time = (TextView) findViewById(R.id.news_page_time);
        news_page_text = (TextView) findViewById(R.id.news_page_text);

        //接收参数
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        time = intent.getStringExtra("time");
        author = intent.getStringExtra("author");
        text = intent.getStringExtra("text");
        url = intent.getStringExtra("url");
        image = intent.getStringArrayListExtra("image");
        video = intent.getStringExtra("video");
        keywords = intent.getStringArrayListExtra("keywords");
        newsID = intent.getStringExtra("newsID");
        category = intent.getStringExtra("category");
        username = intent.getStringExtra("username");

        //Log.d("test","username:" + username);
        //Log.d("test", "newsID:" + newsID);

        //Log.d("test", "read url:" + image.get(0));

        NewsManager.addReadHistory(username, newsID,
                new OneNews(title, author, time, text, url, newsID, category, image, video, keywords));


        news_page_title.setText(title);
        news_page_author.setText(author);
        news_page_time.setText(time);
        news_page_text.setText(text);

        //设置视频
        if(!video.equals("")) {
            RelativeLayout ll_vid = findViewById(R.id.news_page_videos);
            JzvdStd video_play =  new JzvdStd(this);
            video_play.setId(View.generateViewId());
            video_play.setUp(video, title);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 800);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            lp.topMargin = 40;
            lp.bottomMargin = 40;

            ll_vid.addView(video_play, lp);

        }

        //设置图片
        if(image.size() > 0) {
            int[] ids = new int[image.size()];
            int pre_id = -1;
            RelativeLayout ll_pic = findViewById(R.id.news_page_pics);
            for(int i = 0; i < image.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.loading);
                imageView.setAdjustViewBounds(true);
                int newId = View.generateViewId();
                ids[i] = newId;
                imageView.setId(newId);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                if(i == 0) {
                    lp.topMargin = 40;
                }
                else {
                    lp.topMargin = 20;
                    lp.addRule(RelativeLayout.BELOW, pre_id);
                }
                pre_id = imageView.getId();

                if(i == image.size() - 1) {
                    lp.bottomMargin = 40;
                }
                else {
                    lp.bottomMargin = 20;
                }

                ll_pic.addView(imageView, lp);
            }

            for(int i = 0; i < image.size(); i++) {
                ImageView imageView = findViewById(ids[i]);
                String one_pic = image.get(i);
                ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()) {
                    ImageLoader.getInstance().displayImage(one_pic, imageView);
                }
                else {
                    imageView.setImageResource(R.drawable.no_pic);
                }
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_page_menu, menu);

        //检查收藏按钮的起始情况
        MenuItem item = menu.findItem(R.id.news_page_menu_store);
        isStored = NewsManager.searchFavHistory(username, newsID);
        if(isStored) {
            item.setIcon(R.drawable.ic_star_white);
        }
        else {
            item.setIcon(R.drawable.ic_star_border_white);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.news_page_menu_share:

                ShareEntity sharing = new ShareEntity("洋芋新闻", title);
                sharing.setTitle(title);
                sharing.setContent(title);
                if (!url.equals("")) {
                    sharing.setUrl(url);
                }
                if(image.size() > 0) {
                    sharing.setImgUrl(image.get(0));
                    //sharing.setShareBigImg(true);
                }

                ShareUtil.showShareDialog(this,
                        ShareConstant.SHARE_CHANNEL_QQ | ShareConstant.SHARE_CHANNEL_SINA_WEIBO | ShareConstant.SHARE_CHANNEL_WEIXIN_CIRCLE | ShareConstant.SHARE_CHANNEL_WEIXIN_FRIEND,
                        sharing, ShareConstant.REQUEST_CODE);

                break;
            case R.id.news_page_menu_store:
                if(!isStored) { //未收藏
                    Toast.makeText(this, "收藏成功！", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_star_white);
                    NewsManager.addFavHistory(username, newsID,
                            new OneNews(title, author, time, text, url, newsID, category, image, video, keywords));
                    isStored = true;
                } else {  //已收藏
                    Toast.makeText(this, "取消收藏！", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_star_border_white);
                    NewsManager.deleteFavHistory(username, newsID);
                    isStored = false;
                }
                break;
        }
        return false;
    }

    public Bitmap getURLimage(String url) {
        try {
            final URL myurl = new URL(url);
            // 获得连接
            Thread thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
                        conn.setConnectTimeout(6000);//设置超时
                        conn.setDoInput(true);
                        conn.setUseCaches(true);//缓存
                        conn.connect();
                        InputStream is = conn.getInputStream();//获得图片的数据流
                        image_bitmap.add(BitmapFactory.decodeStream(is));//读取图像数据
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thr.start();
            thr.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image_bitmap.get(image_bitmap.size() - 1);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(0, intent);
        finish();
    }

    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewsPageActivity.this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
