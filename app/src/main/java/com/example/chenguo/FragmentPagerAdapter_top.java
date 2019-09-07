package com.example.chenguo;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentPagerAdapter_top extends FragmentPagerAdapter {

    private long baseId = 0;

    List<FragmentNews_each> newslist;


    public FragmentPagerAdapter_top(FragmentManager fm, List<FragmentNews_each> newslist) {
        super(fm);
        this.newslist = newslist;
    }

    @Override
    public int getCount() {
        if(newslist.size() > 0)
            return newslist.size();
        else
            return 10;
    }


    public void changeList(List<FragmentNews_each> newlist) {
        this.newslist = newlist;
    }


    @Override
    public Fragment getItem(int position) {
        if(newslist.get(position).getUserVisibleHint()) {
            return newslist.get(position);
        }
        else {
            return null;
        }
    }

    public void changeId(int n) {
        baseId += getCount() + n;
    }

    @Override
    public long getItemId(int position) {
        return baseId + position;
    }

}
