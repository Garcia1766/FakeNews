package com.java.chenguo;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

public abstract class TagAdapter_delete extends TagAdapter<String> {

    public TagAdapter_delete(List<String> datas) {
        super(datas);
    }


    @Override
    public void onSelected(int position, View view) {
        AppCompatTextView tv = (AppCompatTextView) view;
        tv.setBackgroundResource(R.drawable.search_his_item_background_selected);
        tv.setTextColor(view.getResources().getColor(R.color.color_WhiteSmoke));
    }

    @Override
    public void unSelected(int position, View view) {
        AppCompatTextView tv = (AppCompatTextView) view;
        tv.setBackgroundResource(R.drawable.search_his_item_background);
        tv.setTextColor(view.getResources().getColor(R.color.color_DimGray));
    }
}
