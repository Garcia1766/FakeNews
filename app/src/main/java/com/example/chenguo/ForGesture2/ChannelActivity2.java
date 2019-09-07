package com.example.chenguo.ForGesture2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cheng.channel.*;
import com.example.chenguo.*;
import com.example.chenguo.DataBase.NewsManager;
import com.example.chenguo.DataBase.UserKeyword;
import com.example.chenguo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChannelActivity2 extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private ChannelView channelView;
    private ImageView button_close;
    private ImageView button_reset;

    private final List<String> proto_channels = Arrays.asList("今日", "娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会");
    private final List<String> proto_other_channels = Arrays.asList("头条", "游戏", "时尚", "电影", "数码", "旅游", "养生", "育儿", "历史");

    private String username;

    ArrayList<String> userChannelList;
    ArrayList<String> otherChannelList;
    ArrayList<String> recommendChannelList = new ArrayList<>();

    List<Channel> ch_userChannelList = new ArrayList<>();
    List<Channel> ch_otherChannelList = new ArrayList<>();
    List<Channel> ch_recommendChannelList = new ArrayList<>();


    /**
     * 界面的构造函数
     * @param savedInstanceState
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.chenguo.R.layout.channel2);

        //获取username
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        channelView = findViewById(com.example.chenguo.R.id.channel_view_2);

        //设置关闭键
        button_close = (ImageView) findViewById(R.id.channel_category_close2);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //设置重置键
        button_reset = (ImageView) findViewById(R.id.channel_category_reset2);
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("确定重置为初始状态吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userChannelList.clear();
                        ch_userChannelList.clear();
                        userChannelList.addAll(proto_channels);
                        otherChannelList.clear();
                        ch_otherChannelList.clear();
                        otherChannelList.addAll(proto_other_channels);

                        int i = 0;
                        for (String the_channel : userChannelList) {
                            Channel channel = new Channel(the_channel, 2, (Object) i);
                            i++;
                            ch_userChannelList.add(channel);
                        }

                        for (String the_channel : otherChannelList) {
                            Channel channel = new Channel(the_channel);
                            ch_otherChannelList.add(channel);
                        }

                        saveChannel(false);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        setResult(FragmentNews.CHANNELRESULT, intent);
                        finish();
                    }
                });
            }
        });
        init();
    }


    /**
     * 界面初始化
     * */
    private void init() {

        //从数据库提取数据
        userChannelList = NewsManager.getTopMenu(username);
        otherChannelList = NewsManager.getTopMenuOthers(username);
        if(userChannelList == null || userChannelList.size() == 0) {
            userChannelList = TopMenuChoice.getChoice();
            otherChannelList = TopMenuChoiceOthers.getChoice();
        }
        ArrayList<UserKeyword> recommendList = NewsManager.getUserKeywordPairs(username, 5);
        for(UserKeyword recommend : recommendList) {
            recommendChannelList.add(recommend.getKeyword());
        }

        //将数据添加到频道列表中
        int i = 0;
        for (String the_channel : userChannelList) {
            Channel channel = new Channel(the_channel, 2, (Object) i);
            i++;
            ch_userChannelList.add(channel);
        }

        for (String the_channel : otherChannelList) {
            Channel channel = new Channel(the_channel);
            ch_otherChannelList.add(channel);
        }

        for (String the_channel : recommendChannelList) {
            Channel channel = new Channel(the_channel);
            ch_recommendChannelList.add(channel);
        }

        //将频道列表添加到界面
        channelView.setChannelFixedCount(1);
        channelView.addPlate("我的频道", ch_userChannelList);
        channelView.addPlate("推荐频道", ch_otherChannelList);
        channelView.addPlate("猜你喜欢", ch_recommendChannelList);

        channelView.inflateData();
        channelView.setOnChannelItemClickListener(new ChannelView.OnChannelListener() {
            @Override
            public void channelItemClick(int position, Channel channel) {

            }

            @Override
            public void channelEditFinish(List<Channel> channelList) {

            }

            @Override
            public void channelEditStart() {

            }
        });
    }


    /**
     * 保存频道信息, flag用于表示是否需要从界面中将数据提取下来
     * @param flag
     * */
    private void saveChannel(boolean flag) {
        ArrayList<String> save_user_choices = new ArrayList<>();
        ArrayList<String> save_other_choices = new ArrayList<>();
        if(flag) {
            ch_userChannelList = channelView.getMyChannel();
            ch_otherChannelList = channelView.getOtherChannel().get(0);
        }
        for(Channel channel : ch_userChannelList) {
            save_user_choices.add(channel.getChannelName());
            //Log.d("test", "channel name:" + channel.getChannelName());
        }
        int cnt = 0;
        for(Channel channel : ch_otherChannelList) {
            save_other_choices.add(channel.getChannelName());
            cnt++;
            if(cnt >= 16) {
                break;
            }
        }

        TopMenuChoice.setChoice(save_user_choices);
        TopMenuChoiceOthers.setChoice(save_other_choices);

        NewsManager.updateTopMenu(username, save_user_choices, save_other_choices);
    }


    /**
     * 重载关闭函数
     * */
    @Override
    public void onBackPressed() {
        saveChannel(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(FragmentNews.CHANNELRESULT, intent);
        finish();
    }

    /**
     * 对话框函数，dialogTitle用于文字，onClickListener用于确定键的事件
     * @param dialogTitle
     * @param onClickListener
     * */
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChannelActivity2.this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

}
