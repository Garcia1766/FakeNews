package com.java.chenguo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.Preference;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.java.chenguo.Client.ClientLogout;
import com.java.chenguo.DataBase.NewsManager;
import com.google.android.material.navigation.NavigationView;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    static final int PAGE_ONE = 0;
    static final int PAGE_TWO = 1;
    static final int PAGE_THREE = 2;
    static final int PAGE_FOUR = 3;

    DrawerLayout drawer;
    NavigationView navigationView;
    RadioGroup radioGroup;
    RadioButton radioButton_news;
    RadioButton radioButton_video;
    RadioButton radioButton_hot;
    RadioButton radioButton_apps;
    ViewPager viewPager;
    RelativeLayout nav_header_RL;
    CircleImageView civ;
    TextView name;
    Toolbar toolbar;
    FragmentPagerAdapter_down adapter_news;

    String username = "";

    final private int USERREQUEST = 599;
    final private int USERRESULT = 0;
    final private int LOGOUTREQUEST = 699;
    final private int LOGOUTRESULT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NewsManager.clearReadHistory(username);
        //NewsManager.clearFavHistory(username);
        //NewsManager.clearUserKeywords(username);
        //NewsManager.clearUserDKKeywords(username);


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.nav_clear:
                        openClearDialog();
                        break;
                    case R.id.nav_set:
                        Intent setintent = new Intent(MainActivity.this, SetActivity.class);
                        setintent.putExtra("username", username);
                        startActivity(setintent);
                        break;
                    case R.id.nav_share:
                        ShareEntity sharing = new ShareEntity("洋芋新闻", "洋芋新闻");
                        ShareUtil.showShareDialog(MainActivity.this,
                                ShareConstant.SHARE_CHANNEL_QQ | ShareConstant.SHARE_CHANNEL_SINA_WEIBO | ShareConstant.SHARE_CHANNEL_WEIXIN_CIRCLE | ShareConstant.SHARE_CHANNEL_WEIXIN_FRIEND,
                                sharing, ShareConstant.REQUEST_CODE);
                        break;
                    case R.id.nav_store:
                        Intent storeintent = new Intent(MainActivity.this, StoreHistoryActivity.class);
                        storeintent.putExtra("type", "store");
                        storeintent.putExtra("username", username);
                        startActivity(storeintent);
                        break;
                    case R.id.nav_history:
                        Intent historyintent = new Intent(MainActivity.this, StoreHistoryActivity.class);
                        historyintent.putExtra("type", "history");
                        historyintent.putExtra("username", username);
                        startActivity(historyintent);
                        break;
                    case R.id.nav_money:
                        Intent moneyintent = new Intent(MainActivity.this, MoneyActivity.class);
                        startActivity(moneyintent);
                        break;
                    case R.id.nav_help:
                        showDialog("开发者：陈果、李阳崑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        break;
                    case R.id.nav_feedback:
                        showDialog("请联系chen-g17@mails.tsinghua.edu.cn", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    default:
                        break;
                }
                drawer.closeDrawers();
                return false;
            }
        });

        nav_header_RL = (RelativeLayout) navigationView.getHeaderView(0);
        civ = nav_header_RL.findViewById(R.id.icon_image);
        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals("")) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, USERREQUEST);
                }
                else {
                    Intent intent  = new Intent(MainActivity.this, UserActivity.class);
                    intent.putExtra("username", username);
                    startActivityForResult(intent, LOGOUTREQUEST);
                }
            }
        });

        name = nav_header_RL.findViewById(R.id.username);
        if(username.equals("")) {
            name.setText("游客");
        }
        else {
            name.setText(username);
        }


        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true); //前端代码移植到后端
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);  //前端代码移植到后端
        }
        actionBar.setTitle("洋芋新闻"); //前端代码移植到后端
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);

        radioGroup = (RadioGroup) findViewById(R.id.down_menu);
        radioButton_news = (RadioButton) findViewById(R.id.down_news);
        radioButton_video = (RadioButton) findViewById(R.id.down_video);
        radioButton_hot = (RadioButton) findViewById(R.id.down_hot);
        radioButton_apps = (RadioButton) findViewById(R.id.down_app);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        radioButton_news.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.down_news:
                        viewPager.setCurrentItem(PAGE_ONE);
                        break;
                    case R.id.down_video:
                        viewPager.setCurrentItem(PAGE_TWO);
                        break;
                    case R.id.down_hot:
                        viewPager.setCurrentItem(PAGE_THREE);
                        break;
                    case R.id.down_app:
                        viewPager.setCurrentItem(PAGE_FOUR);
                        break;
                }
            }
        });
        adapter_news = new FragmentPagerAdapter_down(getSupportFragmentManager());

        viewPager.setAdapter(adapter_news);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (viewPager.getCurrentItem()) {
                        case PAGE_ONE:
                            radioButton_news.setChecked(true);
                            break;
                        case PAGE_TWO:
                            radioButton_video.setChecked(true);
                            break;
                        case PAGE_THREE:
                            radioButton_hot.setChecked(true);
                            break;
                        case PAGE_FOUR:
                            radioButton_apps.setChecked(true);
                            break;
                    }
                }
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.main_search);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //对话框
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }


    private void openClearDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_priority_high);
        dialog.setTitle("清理缓存");
        dialog.setMessage("确认要清理缓存吗？");
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(MainActivity.this, "请稍后...", Toast.LENGTH_SHORT).show(); //这里记得修改
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == USERREQUEST && resultCode == 0) {
            username = data.getStringExtra("username");
            if(username.equals("")) {
                name.setText("游客");
            }
            else {
                name.setText(username);
            }
            adapter_news = new FragmentPagerAdapter_down(getSupportFragmentManager());

            viewPager.setAdapter(adapter_news);
            radioButton_news.setChecked(true);

        }
        if(requestCode == LOGOUTREQUEST && resultCode == 0) {
            username = data.getStringExtra("username");
            if(username.equals("")) {
                name.setText("游客");
            }
            else {
                name.setText(username);
            }
            adapter_news = new FragmentPagerAdapter_down(getSupportFragmentManager());

            viewPager.setAdapter(adapter_news);
            radioButton_news.setChecked(true);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        if(!username.equals("")) {
            ClientLogout.sendLogoutRequest(username);
        }
        super.finish();
    }
}
