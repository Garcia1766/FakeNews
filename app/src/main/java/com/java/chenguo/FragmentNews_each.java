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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.chenguo.DataGet.GrabData;
import com.java.chenguo.DataGet.OneNews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentNews_each extends Fragment {

    private String channel;
    private List<String> proto_channels = Arrays.asList("娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会");
    final static int READREQUEST = 99;
    final static int READRESULT = 0;

    boolean isViewShown = false;

    private ListView listNews;
    private RefreshLayout refresher;
    NewsList_Adapter adapter;
    private View m_listViewFooter;
    private RelativeLayout noConncectText;
    private ImageView noConnectRefresh;
    private NetworkStateReceiver netWorkStateReceiver;

    private String end_time;
    private String username;

    private GrabData get_data;

    public View view;

    public FragmentNews_each() {
        //Log.d("test", "创建fragment_news_each");
        get_data = new GrabData();
    }

    public String getChannel() {
        return channel;
    }

    public void set_username(String username) {
        this.username = username;
    }

    public int getNewsNumInside() {
        return get_data.returnNews.size();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            //Log.d("test", "要 来 了, channel是" + channel);
            isViewShown = true;
            if(get_data.returnNews.size() == 0 && adapter != null) {
                //Log.d("test", "进入hint的grab, channel是" + channel);
                long start_time = System.currentTimeMillis();
                Log.d("test111",String.valueOf(0));
                end_time = topRefresh();
                getData(20, true, end_time);
                Log.d("test111", String.valueOf(System.currentTimeMillis() - start_time));
                //adapter.notifyDataSetChanged();
            }
        }
    }

    private void getData(int how_much, boolean flag, String endtime) {
        if(proto_channels.contains(channel))
            get_data.sendRequestWithOkHttp(String.valueOf(how_much), "", endtime, "", channel, getActivity(), adapter, flag, false, username);
        else
            get_data.sendRequestWithOkHttp(String.valueOf(how_much), "", endtime, channel, "" , getActivity(), adapter, flag, false, username);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        username = ((MainActivity) getActivity()).username;
        //Log.d("test", "each username:" + username);

        Bundle bundle = getArguments();
        channel = bundle.getString("name");
        if(view != null) {
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }
        else {
            if (netWorkStateReceiver == null) {
                netWorkStateReceiver = new NetworkStateReceiver();
            }
            view = inflater.inflate(R.layout.fragment_news_list, container, false);
            initView();
        }

        return view;
    }

    private void initView() {
        noConncectText = view.findViewById(R.id.list_404);
        noConnectRefresh = view.findViewById(R.id.list_404_refresh);
        refresher = (RefreshLayout) view.findViewById(R.id.refresh_layout);
        m_listViewFooter = LayoutInflater.from(getContext()).inflate(R.layout.listview_footer, null, false);
        listNews = (ListView) view.findViewById(R.id.list_news);
        adapter = new NewsList_Adapter(get_data.returnNews, getContext(), username);
        listNews.setAdapter(adapter);
        //FLog.d("test", "initView成功, channel是" + channel);

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
                        }  else {
                            Toast.makeText(getContext(), "刷新失败了orz", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                refresher.setVisibility(View.GONE);
                return;
        }

        init();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == READREQUEST && resultCode == 0) {
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {

        if (isViewShown) {
            //Log.d("test", "请求发生");
            long start_time = System.currentTimeMillis();
            end_time = topRefresh();
            Log.d("test222", String.valueOf(0));
            getData(20, true, end_time);
            Log.d("test222", String.valueOf(System.currentTimeMillis() - start_time));
        }


        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!netWorkStateReceiver.isConnected) {
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
                Intent intent = new Intent(getContext(), NewsPageActivity.class);
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
                if (!netWorkStateReceiver.isConnected) {
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

                listNews.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        end_time = downRefresh();
                        get_data.returnNews.clear();
                        adapter.notifyDataSetChanged();
                        getData(20, false, end_time);
                        Toast.makeText(getContext(), "刷新完成！", Toast.LENGTH_SHORT).show();
                        //adapter.notifyDataSetChanged();

                        refresher.setRefreshing(false);
                    }

                }, 1500);
            }
        });

        //底部上拉刷新
        refresher.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!netWorkStateReceiver.isConnected) {
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

                listNews.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        end_time = downRefresh();
                        int old_size = get_data.returnNews.size();
                        getData(15, false, end_time);
                        int new_size = get_data.returnNews.size();
                        if(old_size < new_size)
                            Toast.makeText(getContext(), "加载完成！", Toast.LENGTH_SHORT).show();
                        else if (old_size == new_size)
                            Toast.makeText(getContext(), "已经到底了噢", Toast.LENGTH_SHORT).show();
                        //adapter.notifyDataSetChanged();
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

