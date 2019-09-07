package com.example.chenguo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cheng.channel.*;
import com.example.chenguo.*;
import com.example.chenguo.DataBase.NewsManager;
import com.example.chenguo.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SetHisActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private TagFlowLayout tagFlowLayout;
    private TagAdapter_delete adapter;
    private ImageView button_close;
    private ImageView button_delete;

    private String username;

    ArrayList<String> data;

    ArrayList<String> data_pre_clear = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.chenguo.R.layout.set_his);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        tagFlowLayout = (TagFlowLayout) findViewById(R.id.search_history_tfl);

        data = NewsManager.showSearchHistory(username);

        adapter = new TagAdapter_delete(data) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(SetHisActivity.this).inflate(R.layout.search_history_text, tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        tagFlowLayout.setAdapter(adapter);

        button_close = (ImageView) findViewById(R.id.set_his_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        button_delete =(ImageView) findViewById(R.id.set_his_delete);

        init();
    }

    private void init() {

        //选中
        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                data_pre_clear.clear();
                for(Integer i : selectPosSet) {
                    data_pre_clear.add(data.get(i));
                }
            }
        });

        //删除
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("确定要删除选定历史记录？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(String str : data_pre_clear) {
                            data.remove(str);
                            NewsManager.deleteSearchHistory(username, str);
                            adapter.notifyDataChanged();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SearchActivity.class);
        setResult(SearchActivity.CHANNELRESULT, intent);
        finish();
    }

    //对话框
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetHisActivity.this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

}
