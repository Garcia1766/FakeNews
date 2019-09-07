package com.java.chenguo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.chenguo.DataBase.NewsManager;
import com.java.chenguo.DataBase.UserKeyword;
import com.java.chenguo.DataGet.GrabData;
import com.java.chenguo.DataGet.OneNews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class FragmentHot extends Fragment {

    public FragmentHot() {}

    private ListView listNews;
    private RefreshLayout refresher;
    private NewsList_Adapter adapter;
    private View m_listViewFooter;
    private RelativeLayout noConncectText;
    private ImageView noConnectRefresh;
    private NetworkStateReceiver netWorkStateReceiver;

    private GrabData get_data;
    String end_time;
    String username;

    View view;

    boolean isloading = false;

    final static int READREQUEST = 199;
    final static int READRESULT = 0;


    /**
     * 界面构造函数
     * @param inflater
     * @param container
     * @param savedInstanceState
     * */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        username = ((MainActivity) getActivity()).username;

        //用于复用fragment
        if(view != null) {
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
            adapter.notifyDataSetChanged();
        }
        else {
            if (netWorkStateReceiver == null) {
                netWorkStateReceiver = new NetworkStateReceiver();
            }
            view = inflater.inflate(R.layout.fragment_hot, container, false);
            initView();
        }

        return view;
    }

    /**
     * 初始化界面
     * */
    private void initView() {
        //获取所有界面信息
        listNews = (ListView) view.findViewById(R.id.hot_list_news);
        refresher = (RefreshLayout) view.findViewById(R.id.hot_refresh_layout);
        m_listViewFooter = LayoutInflater.from(getContext()).inflate(R.layout.listview_footer, null, false);
        get_data = new GrabData();
        adapter = new NewsList_Adapter(get_data.returnNews, getContext(), username);
        listNews.setAdapter(adapter);
        noConncectText = view.findViewById(R.id.list_404);
        noConnectRefresh = view.findViewById(R.id.list_404_refresh);

        //检查网络
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()) {
            noConncectText.setVisibility(View.VISIBLE);
            noConnectRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (netWorkStateReceiver.isConnected) {
                        noConncectText.setVisibility(View.GONE);
                        refresher.setVisibility(View.VISIBLE);
                        init();
                    } else {
                        Toast.makeText(getContext(), "刷新失败了orz", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            refresher.setVisibility(View.GONE);
        }

        init();
    }

    /**
     * 初始化界面内部逻辑
     * */
    private void init() {
        end_time = topRefresh();
        getData(end_time, 20, adapter, true);

        //设置列表的响应
        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //断网的情况
                if(!netWorkStateReceiver.isConnected){
                    noConncectText.setVisibility(View.VISIBLE);
                    noConnectRefresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (netWorkStateReceiver.isConnected) {
                                noConncectText.setVisibility(View.GONE);
                                refresher.setVisibility(View.VISIBLE);
                                init();
                            } else {
                                Toast.makeText(getContext(), "刷新失败了orz", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    refresher.setVisibility(View.GONE);
                    return;
                }

                //启动news_page_activity
                Intent intent = new Intent(getActivity(), NewsPageActivity.class);
                OneNews item = get_data.returnNews.get(i);
                //设置参数
                intent.putExtra("title", item.getTitle());
                intent.putExtra("time", item.getPublishTime());
                intent.putExtra("author", item.getPublisher());
                intent.putExtra("text", item.getContent());
                intent.putExtra("url", item.getUrl());
                intent.putExtra("image", item.getImage());
                intent.putExtra("video", item.getVideo());
                intent.putExtra("keywords", item.getKeywords());
                intent.putExtra("newsID", item.getNewsID());
                intent.putExtra("category", item.getCategory());
                intent.putExtra("username", username);

                startActivityForResult(intent, READREQUEST);
            }
        });

        //设置刷新
        listNews.addFooterView(m_listViewFooter);

        //顶部下拉刷新
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //断网的情况
                if(!netWorkStateReceiver.isConnected){
                    noConncectText.setVisibility(View.VISIBLE);
                    noConnectRefresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (netWorkStateReceiver.isConnected) {
                                noConncectText.setVisibility(View.GONE);
                                refresher.setVisibility(View.VISIBLE);
                                init();
                            } else {
                                Toast.makeText(getContext(), "刷新失败了orz", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    refresher.setVisibility(View.GONE);
                    listNews.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "没有连接到网络", Toast.LENGTH_SHORT).show();
                            refresher.setRefreshing(false);
                        }

                    }, 1500);
                    return;
                }

                if(isloading)
                    return;

                isloading = true;

                //不断网的情况
                listNews.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        end_time = downRefresh();
                        get_data.returnNews.clear();
                        adapter.notifyDataSetChanged();
                        getData(end_time, 20, adapter, false);
                        Toast.makeText(getContext(), "新热榜推荐！", Toast.LENGTH_SHORT).show();
                        isloading = false;
                        refresher.setRefreshing(false);
                    }

                }, 1500);
            }
        });

        //底部上拉刷新
        refresher.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                //断网的情况
                if(!netWorkStateReceiver.isConnected){
                    noConncectText.setVisibility(View.VISIBLE);
                    noConnectRefresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (netWorkStateReceiver.isConnected) {
                                noConncectText.setVisibility(View.GONE);
                                refresher.setVisibility(View.VISIBLE);
                                init();
                            }
                        }
                    });
                    refresher.setVisibility(View.GONE);
                    listNews.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "没有连接到网络", Toast.LENGTH_SHORT).show();
                            refresher.setRefreshing(false);
                        }

                    }, 1500);
                    return;
                }

                if(isloading)
                    return;

                isloading = true;

                //不断网的情况
                listNews.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        end_time = downRefresh();
                        int old_size = get_data.returnNews.size();
                        getData(end_time, 15, adapter, false);
                        int new_size = get_data.returnNews.size();
                        if(old_size < new_size)
                            Toast.makeText(getContext(), "加载完成！", Toast.LENGTH_SHORT).show();
                        else if (old_size == new_size)
                            Toast.makeText(getContext(), "已经到底了噢", Toast.LENGTH_SHORT).show();
                        //adapter.notifyDataSetChanged();
                        isloading = false;
                        refresher.setLoading(false);
                    }
                }, 1500);


            }

            @Override
            public void setFooterView(boolean isLoading) {
                if (isLoading) {
                    listNews.removeFooterView(m_listViewFooter);
                    listNews.addFooterView(m_listViewFooter);
                } else {
                    listNews.removeFooterView(m_listViewFooter);
                }
            }
        });

        refresher.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_light));
    }


    //顶部刷新，直接更新到最新时间
    private String topRefresh() {
        StringBuilder sb = new StringBuilder();
        sb.append("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(sb.toString());
        String dateString =sdf.format(new Date());

        return dateString;
    }

    //底部刷新，小时数-3
    private String downRefresh() {
        String last_start_time;
        if(get_data.returnNews.size() > 0)
            last_start_time = get_data.returnNews.get(get_data.returnNews.size() - 1).getPublishTime();
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat(sb.toString());
            String dateString =sdf.format(new Date());
            return dateString;
        }
        String new_last_time = "";
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(last_start_time));
            long timeInMillis = c.getTimeInMillis() - 3000;
            new_last_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeInMillis));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new_last_time;
    }


    //根据历史记录和收藏夹提取关键词并得到data
    private void getData(String end_time, int num, NewsList_Adapter adapter, boolean flag) {
        ArrayList<UserKeyword> hot_words = NewsManager.getUserKeywordPairs(username, 3);
        long[] times = new long[3];
        long total_times = 0;
        long[] get_nums = new long[3];


        //还没有热词
        if(hot_words.size() == 0) {
            String s = "";
            get_data.sendRequestWithOkHttp(String.valueOf(num), "", end_time, s, "", getActivity(), adapter, flag, true, username);
        }
        //有热词
        else {
            for(int i = 0; i < hot_words.size(); i++) {
                times[i] = hot_words.get(i).getTimes();
                total_times  = total_times + times[i];
            }
            for(int i = 0; i < hot_words.size(); i++) {
                double proportion = ((double) times[i]) / ((double) total_times);
                get_nums[i] = Double.valueOf(num * proportion).longValue();

            }

            for(int i = 0; i < hot_words.size(); i++) {
                String s = hot_words.get(i).getKeyword();
                //Log.d("test", "fragment中，第" + i + "个log点前");
                get_data.sendRequestWithOkHttp(String.valueOf(get_nums[i]), "", end_time, s, "", getActivity(), null, flag, false, username);
                //Log.d("test", "fragment中，第" + i + "个log点后");
            }
            String s = hot_words.get(hot_words.size() - 1).getKeyword();
            //Log.d("test", "fragment中，第" + (hot_words.size() - 1) + "个log点前");
            get_data.sendRequestWithOkHttp(String.valueOf(get_nums[hot_words.size() - 1]), "", end_time, s, "", getActivity(), adapter, flag, false, username);
            //Log.d("test", "fragment中，第" + (hot_words.size() - 1) + "个log点后");
            //adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == READREQUEST && resultCode == 0) {
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetworkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(netWorkStateReceiver, filter);

        adapter.notifyDataSetChanged();

        super.onResume();
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(netWorkStateReceiver);
        super.onPause();
    }
}


