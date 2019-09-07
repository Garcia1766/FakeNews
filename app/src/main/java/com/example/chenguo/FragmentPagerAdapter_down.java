package com.example.chenguo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentPagerAdapter_down extends FragmentPagerAdapter {

    private final int PAGE_NUM = 4;
    private FragmentNews fg_news = null;
    private FragmentVideo fg_video = null;
    private FragmentHot fg_hot = null;
    private FragmentApps fg_apps = null;

    public FragmentPagerAdapter_down(FragmentManager fm) {
        super(fm);
        fg_news = new FragmentNews();
        fg_video = new FragmentVideo();
        fg_hot = new FragmentHot();
        fg_apps = new FragmentApps();
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = fg_news;
                break;
            case MainActivity.PAGE_TWO:
                fragment = fg_video;
                break;
            case MainActivity.PAGE_THREE:
                fragment = fg_hot;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = fg_apps;
                break;
        }
        return fragment;
    }

}
