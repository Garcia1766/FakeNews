package com.java.chenguo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.ArrayList;

import com.java.chenguo.ForGesture2.ChannelActivity2;

public class FragmentNews extends Fragment {

    private View view;
    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private HorizontalScrollView hsv;
    private List<FragmentNews_each> newsList = new ArrayList<>();
    FragmentPagerAdapter_top adapter;

    private ImageView button_more_columns; // 标题右边的+号
    private LayoutInflater inflater; //辅助变量

    private String username;

    public final static int CHANNELREQUEST = 1; // 请求码
    public final static int CHANNELRESULT = 10; // 返回码

    public FragmentNews() {
        //Log.d("test", "创建fragment_news");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        username = ((MainActivity) getActivity()).username;

        if(view == null) {
            //Log.d("test", "真的加载了吗？");
            view = inflater.inflate(R.layout.top_news_part, container, false);
            this.inflater = inflater;
            initView(inflater);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
            for(FragmentNews_each fg : adapter.newslist) {
                fg.set_username(username);
                if(fg.adapter != null) {
                    fg.adapter.set_username(username);
                    fg.adapter.notifyDataSetChanged();
                }
            }
        }

        return view;
    }

    private void initView(LayoutInflater inflater) {
        radioGroup = (RadioGroup) view.findViewById(R.id.top_radioGroup);
        hsv = (HorizontalScrollView) view.findViewById(R.id.news_scroll);
        viewPager = (ViewPager) view.findViewById(R.id.news_view_pager);
        //Log.d("test", "真的加载了哦");
        adapter = new FragmentPagerAdapter_top(getFragmentManager(), newsList);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                viewPager.setCurrentItem(i);
            }
        });

        setTopView();

        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton rbButton = (RadioButton) radioGroup.getChildAt(position);
                rbButton.setChecked(true);
                int left = rbButton.getLeft();
                int width = rbButton.getMeasuredWidth();
                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int screenWidth = metrics.widthPixels;
                int len = left + width / 2 - screenWidth / 2;
                hsv.smoothScrollTo(len, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //+号事件响应
        button_more_columns = (ImageView) view.findViewById(R.id.button_more_columns);

        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent_channel = new Intent(getContext(), ChannelActivity.class);
                Intent intent_channel = new Intent(getContext(), ChannelActivity2.class);
                intent_channel.putExtra("username", username);
                startActivityForResult(intent_channel, CHANNELREQUEST);
            }
        });


    }

//    private void clearFragment() {
//        FragmentManager fm = getFragmentManager();
//        List<Fragment> list = fm.getFragments();
//        for(Fragment f:list) {
//            Log.d("test", "这里有一个"+f.getClass());
//        }
//        FragmentTransaction ft = fm.beginTransaction();
//        for(FragmentNews_each fge:newsList) {
//            fge.onDestroy();
//            ft.remove(fge);
//        }
//        list = fm.getFragments();
//        for(Fragment f:list) {
//            Log.d("test", "这里有一个"+f.getClass());
//        }
//    }

    private void setTopView() {
        newsList.clear();
        radioGroup.removeAllViews();
        List<String> choices = TopMenuChoice.getChoice();
        for(int i = 0; i < choices.size(); i++) {
            RadioButton rb = (RadioButton) inflater.inflate(R.layout.top_news_radiobutton, null);
            rb.setId(i);
            rb.setText(choices.get(i));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(rb, params);
        }
        adapter.notifyDataSetChanged();
        radioGroup.check(0);

        for(int i = 0; i < choices.size(); i++) {
            FragmentNews_each fg = new FragmentNews_each();
            Bundle bundle = new Bundle();
            bundle.putString("name", choices.get(i));
            fg.setArguments(bundle);
            newsList.add(fg);
        }
        adapter.changeId(1);
        adapter.notifyDataSetChanged();
        adapter.changeList(newsList);
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CHANNELREQUEST:
                if(resultCode == CHANNELRESULT) {
                    setTopView();
                }
                break;
        }
    }
}
