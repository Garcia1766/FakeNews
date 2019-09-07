package com.example.chenguo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chenguo.DataBase.NewsManager;
import com.example.chenguo.DataBase.SearchHistory;
import com.example.chenguo.DataGet.GrabData;
import com.example.chenguo.DataGet.OneNews;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;

    private RelativeLayout RFL_layout;
    private ListView listNews;
    private TagFlowLayout tagFlowLayout;
    private RefreshLayout refresher;
    private NewsList_Adapter adapter;
    private TagAdapter adapter_for_his;
    private View m_listViewFooter;
    private ImageView clear_all_button;
    private RelativeLayout noConncectText;
    private ImageView noConnectRefresh;
    private NetworkStateReceiver netWorkStateReceiver;

    private GrabData get_data;
    private ArrayList<String> his_data;

    private String username;

    static final int CHANNELREQUEST = 499;
    static final int CHANNELRESULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetworkStateReceiver();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        toolbar = (Toolbar) findViewById(R.id.search_tool_bar);
        setSupportActionBar(toolbar);

        final ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listNews = (ListView) findViewById(R.id.search_list_news);
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.search_history_tfl);
        RFL_layout = (RelativeLayout) findViewById(R.id.search_history);
        clear_all_button = (ImageView) findViewById(R.id.search_delete_all);
        refresher = (RefreshLayout) findViewById(R.id.search_refresh_layout);
        m_listViewFooter = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_footer, null, false);
        noConncectText = findViewById(R.id.list_404);
        noConnectRefresh = findViewById(R.id.list_404_refresh);

        //检查网络
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()) {
            tagFlowLayout.setVisibility(View.VISIBLE);
            noConncectText.setVisibility(View.VISIBLE);
            noConnectRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(netWorkStateReceiver.isConnected){
                        noConncectText.setVisibility(View.GONE);
                        tagFlowLayout.setVisibility(View.VISIBLE);
                        refresher.setVisibility(View.GONE);
                        init();
                    } else {
                        Toast.makeText(getApplicationContext(), "刷新失败了orz", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            refresher.setVisibility(View.GONE);
            return;
        }

        init();
    }

    private void init() {
        get_data = new GrabData();
        his_data = NewsManager.showSearchHistory(username);

        tagFlowLayout.setVisibility(View.VISIBLE);

        adapter = new NewsList_Adapter(get_data.returnNews, this, username);
        adapter_for_his = new TagAdapter<String>(his_data) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_history_text, tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        listNews.setAdapter(adapter);
        tagFlowLayout.setAdapter(adapter_for_his);

        //设置刷新
        listNews.addFooterView(m_listViewFooter);

        //设置清除键
        clear_all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, SetHisActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, CHANNELREQUEST);

            }
        });

        refresher.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = (MenuItem) menu.findItem(R.id.search_search);
        SearchView searchView = (SearchView) item.getActionView();

        item.collapseActionView();
        item.expandActionView();

        searchView.setIconifiedByDefault(false);
        searchView.setIconified(true);
        searchView.setBackgroundColor(getResources().getColor(R.color.color_White));
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入关键词...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText tvv = (EditText) searchView.findViewById(id);
        tvv.setHintTextColor(getResources().getColor(R.color.color_Black));
        tvv.setTextColor(getResources().getColor(R.color.color_Black));

        int idd = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView ivv = (ImageView) searchView.findViewById(idd);
        ivv.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_Black)));

        idd = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ivv = (ImageView) searchView.findViewById(idd);
        ivv.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_Black)));

        idd = searchView.getContext().getResources().getIdentifier("android:id/search_go_btn", null, null);
        ivv = (ImageView) searchView.findViewById(idd);
        ivv.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_Black)));



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!netWorkStateReceiver.isConnected) {
                    return false;
                }

                RFL_layout.setVisibility(View.GONE);
                refresher.setVisibility(View.VISIBLE);
                get_data.returnNews.clear();
                NewsManager.addSearchHistory(username, s);
                if(!his_data.contains(s)) {
                    his_data.add(0, s);
                }

                final String ss = s;
                StringBuilder sb = new StringBuilder();
                sb.append("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat(sb.toString());
                String dateString = sdf.format(new Date());
                get_data.sendRequestWithOkHttp(String.valueOf(20), "", dateString, s, "", SearchActivity.this, adapter, false, false, username);

                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(!netWorkStateReceiver.isConnected) {
                            tagFlowLayout.setVisibility(View.VISIBLE);
                            noConncectText.setVisibility(View.VISIBLE);
                            noConnectRefresh.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(netWorkStateReceiver.isConnected){
                                        noConncectText.setVisibility(View.GONE);
                                        tagFlowLayout.setVisibility(View.VISIBLE);
                                        refresher.setVisibility(View.GONE);
                                        init();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "刷新失败了orz", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            refresher.setVisibility(View.GONE);
                            return;
                        }

                        //启动news_page_activity
                        Intent intent = new Intent(SearchActivity.this, NewsPageActivity.class);
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

                        startActivity(intent);
                    }
                });

                //顶部下拉刷新
                refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(!netWorkStateReceiver.isConnected) {
                            tagFlowLayout.setVisibility(View.VISIBLE);
                            noConncectText.setVisibility(View.VISIBLE);
                            noConnectRefresh.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(netWorkStateReceiver.isConnected){
                                        noConncectText.setVisibility(View.GONE);
                                        tagFlowLayout.setVisibility(View.VISIBLE);
                                        refresher.setVisibility(View.GONE);
                                        init();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "刷新失败了orz", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            refresher.setVisibility(View.GONE);
                            listNews.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SearchActivity.this, "没有连接到网络", Toast.LENGTH_SHORT).show();
                                    refresher.setRefreshing(false);
                                }

                            }, 1500);
                            return;
                        }

                        listNews.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                get_data.returnNews.clear();
                                adapter.notifyDataSetChanged();
                                get_data.sendRequestWithOkHttp(String.valueOf(20), "", topRefresh(), ss, "", SearchActivity.this, adapter, false, false, username);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "刷新完成！", Toast.LENGTH_SHORT).show();
                                refresher.setRefreshing(false);
                            }

                        }, 1500);
                    }
                });

                //底部上拉刷新
                refresher.setOnLoadListener(new RefreshLayout.OnLoadListener() {
                    @Override
                    public void onLoad() {
                        if(!netWorkStateReceiver.isConnected) {
                            tagFlowLayout.setVisibility(View.VISIBLE);
                            noConncectText.setVisibility(View.VISIBLE);
                            noConnectRefresh.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(netWorkStateReceiver.isConnected){
                                        noConncectText.setVisibility(View.GONE);
                                        tagFlowLayout.setVisibility(View.VISIBLE);
                                        refresher.setVisibility(View.GONE);
                                        init();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "刷新失败了orz", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            refresher.setVisibility(View.GONE);
                            listNews.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SearchActivity.this, "没有连接到网络", Toast.LENGTH_SHORT).show();
                                    refresher.setRefreshing(false);
                                }

                            }, 1500);
                            return;
                        }

                        listNews.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int old_size = get_data.returnNews.size();
                                get_data.sendRequestWithOkHttp(String.valueOf(15), "", downRefresh(), ss, "", SearchActivity.this, adapter, false, false, username);
                                int new_size = get_data.returnNews.size();
                                if(old_size < new_size)
                                    Toast.makeText(SearchActivity.this, "加载完成！", Toast.LENGTH_SHORT).show();
                                else if (old_size == new_size)
                                    Toast.makeText(SearchActivity.this, "已经到底了噢", Toast.LENGTH_SHORT).show();



                                adapter.notifyDataSetChanged();
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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!netWorkStateReceiver.isConnected) {
                    return false;
                }
                if(s.equals("")) {
                    refresher.setVisibility(View.GONE);
                    RFL_layout.setVisibility(View.VISIBLE);
                    adapter.dataClear();
                    adapter_for_his.notifyDataChanged();
                }
                return false;
            }
        });

        //设置历史记录区
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                searchView.setQuery(his_data.get(position), true);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.search_back) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    //对话框
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CHANNELREQUEST:
                if(resultCode == CHANNELRESULT) {
                    ArrayList<String> tmp = NewsManager.showSearchHistory(username);
                    his_data.clear();
                    for(String str : tmp) {
                        his_data.add(str);
                    }
                    adapter_for_his.notifyDataChanged();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetworkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(netWorkStateReceiver, filter);
        super.onResume();
    }

    @Override
    public void onPause() {
        this.unregisterReceiver(netWorkStateReceiver);
        super.onPause();
    }
}
