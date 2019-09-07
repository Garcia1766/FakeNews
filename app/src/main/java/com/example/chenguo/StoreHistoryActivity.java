package com.example.chenguo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chenguo.DataBase.NewsManager;
import com.example.chenguo.DataGet.OneNews;

import java.util.ArrayList;

public class StoreHistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listNews;
    private NewsList_Adapter adapter;

    public ArrayList<OneNews> data;
    public String type; // 只有store和history两个类型
    private String username;
    boolean isConnected;

    final static int READREQUEST = 299;
    final static int READRESULT = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_history);

        toolbar = (Toolbar) findViewById(R.id.store_history_tool_bar);
        listNews = (ListView) findViewById(R.id.store_history_list_news);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        }

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        type = intent.getStringExtra("type");
        Log.d("test", "username:" + username);
        if(type.equals("store")) {
            actionBar.setTitle("收藏");
        } else if (type.equals("history")) {
            actionBar.setTitle("历史记录");
        }
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);

        showList();

    }


    void showList() {
        if (type.equals("store")) {
            data = NewsManager.showFavHistory(username);
        } else if (type.equals("history")) {
            Log.d("test", "username in his:" + username);
            data = NewsManager.showReadHistory(username);
        }

        adapter = new NewsList_Adapter(data, this, username);
        listNews.setAdapter(adapter);
        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //启动news_page_activity
                Intent intent = new Intent(StoreHistoryActivity.this, NewsPageActivity.class);
                OneNews item = data.get(i);
                //设置参数
                intent.putExtra("title", item.getTitle());
                intent.putExtra("time", item.getPublishTime());
                intent.putExtra("author", item.getPublisher());
                intent.putExtra("text", item.getContent());
                intent.putExtra("image", item.getImage());
                intent.putExtra("video", item.getVideo());
                intent.putExtra("keywords", item.getKeywords());
                intent.putExtra("newsID", item.getNewsID());
                intent.putExtra("category", item.getCategory());
                intent.putExtra("username", username);

                startActivityForResult(intent, READREQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.storehistory_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.store_history_delete) {
            showDialog("确定要删除所有记录？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(type.equals("store")) {
                        NewsManager.clearFavHistory(username);
                        data.clear();
                        adapter.notifyDataSetChanged();
                    }
                    else if (type.equals("history")) {
                        NewsManager.clearReadHistory(username);
                        data.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == READREQUEST && resultCode == 0) {
            adapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    //对话框
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StoreHistoryActivity.this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }


    @Override
    public void onResume() {
        ArrayList<OneNews> new_data = new ArrayList<>();
        if (type.equals("store")) {
            new_data = NewsManager.showFavHistory(username);
        } else if (type.equals("history")) {
            new_data = NewsManager.showReadHistory(username);
        }

        data.clear();
        for(OneNews oneNews:new_data){
            data.add(oneNews);
        }

        adapter.notifyDataSetChanged();

        super.onResume();
    }

}
