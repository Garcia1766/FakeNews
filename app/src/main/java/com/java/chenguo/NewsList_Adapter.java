package com.java.chenguo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.chenguo.DataBase.NewsManager;
import com.java.chenguo.DataGet.OneNews;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class NewsList_Adapter extends BaseAdapter {
    private List<OneNews> data;
    Context context;

    String url;
//    String url_3[];
    OneNews title;
    View view;
    ViewHolder viewHolder;
//    LinearLayout LL;
    private ImageLoader imageLoader;
    boolean hasPic = true;
    private String username;

    public NewsList_Adapter(List<OneNews> data, Context context, String username) {
        this.data = data;
        this.context = context;
        this.username = username;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault((Activity) this.context));
    }

    public void set_username(String username) {
        this.username = username;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        title = (OneNews) getItem(position);
        //Log.d("test", "调用getView, title是" + title.getTitle());
//        url_3 = new String[3];

        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.fragment_news_list_item, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.news_title);
            viewHolder.source = (TextView) view.findViewById(R.id.news_source);
            viewHolder.time = (TextView) view.findViewById(R.id.news_time);
            viewHolder.image = (ImageView) view.findViewById(R.id.news_image);
//            viewHolder.image_3 = new ImageView[3];
//            viewHolder.image_3[0] = (ImageView) view.findViewById(R.id.news_image_1);
//            viewHolder.image_3[1] = (ImageView) view.findViewById(R.id.news_image_2);
//            viewHolder.image_3[2] = (ImageView) view.findViewById(R.id.news_image_3);
            viewHolder.video = (JzvdStd) view.findViewById(R.id.news_video);
            viewHolder.delete = (ImageView) view.findViewById(R.id.news_delete);
//            LL = (LinearLayout) view.findViewById(R.id.news_image_group);
            view.setTag(viewHolder);

            //检查是否已经阅读过
            boolean isReaded = NewsManager.searchReadHistory(username, title.getNewsID());
            if (isReaded) {
                viewHolder.title.setTextColor(view.getResources().getColor(R.color.color_Gray));
            } else {
                viewHolder.title.setTextColor(view.getResources().getColor(R.color.color_Black));
            }
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
            if(!title.getNewsID().equals(viewHolder.newsID)) {
//                viewHolder.image.setVisibility(View.VISIBLE);
//                LL.setVisibility(View.GONE);
//                for(int i = 0; i < 3; i++) {
//                    viewHolder.image_3[i].setVisibility(View.GONE);
//                }
                viewHolder.image.setImageResource(0);
            }
            if(title.getVideo().equals("")) {
                hasPic = true;
                viewHolder.video.setVisibility(View.GONE);
            }
            //检查是否已经阅读过
            boolean isReaded = NewsManager.searchReadHistory(username, title.getNewsID());
            if (isReaded) {
                viewHolder.title.setTextColor(view.getResources().getColor(R.color.color_Gray));
            }
            else {
                viewHolder.title.setTextColor(view.getResources().getColor(R.color.color_Black));
            }
        }

        viewHolder.title.setText(title.getTitle());
        viewHolder.source.setText(title.getPublisher());
        viewHolder.time.setText(title.getPublishTime());
        viewHolder.newsID = title.getNewsID();

        url = "";

        if(!title.getVideo().equals("")) {
            hasPic = false;
            viewHolder.video.setVisibility(View.VISIBLE);
            viewHolder.video.setUp(title.getVideo(), title.getTitle());
            //viewHolder.video.startVideo();
        }

        if(title.getImage().size() != 0 && hasPic) {
//            if(title.getImage().size() < 3) {
//                viewHolder.image.setVisibility(View.VISIBLE);
//                LL.setVisibility(View.GONE);
//                for(int i = 0; i < 3; i++) {
//                    viewHolder.image_3[i].setVisibility(View.GONE);
//                }
                viewHolder.image.setImageResource(R.drawable.loading);
                url = title.getImage().get(0);
                //Log.d("test", "image url:" + url);
                ViewGroup.LayoutParams lp = viewHolder.image.getLayoutParams();
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                viewHolder.image.setLayoutParams(lp);
                ImageLoader.getInstance().displayImage(url, viewHolder.image);
//            }
//            else {
//                viewHolder.image.setVisibility(View.GONE);
//                LL.setVisibility(View.VISIBLE);
//                for(int i = 0; i < 3; i++) {
//                    viewHolder.image_3[i].setVisibility(View.VISIBLE);
//                }
//                Log.d("test", "第三个是否visible:" + viewHolder.image_3[2].getVisibility());
//                for(int i = 0; i < 3; i++) {
//                    viewHolder.image_3[i].setImageResource(R.drawable.loading);
//                    url_3[i] = title.getImage().get(i);
//                    ImageLoader.getInstance().displayImage(url_3[i], viewHolder.image_3[i]);
//                }
//
//            }
        }
        else {
            viewHolder.image.setImageResource(0);
            ViewGroup.LayoutParams lp = viewHolder.image.getLayoutParams();
            lp.width = 0;
            lp.height = 0;
            viewHolder.image.setLayoutParams(lp);
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("test", "从position来的标题:" + data.get(position).getTitle());
                if (!context.getClass().toString().equals("class com.java.chenguo.StoreHistoryActivity")) {
                    showDialog("不喜欢这条新闻吗？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Log.d("test", "dk, username:" + username);
                            //Log.d("test", "dk, size:" + data.get(position).getKeywords().size());
                            NewsManager.addUserDKKeywords(username, data.get(position).getKeywords());
                            //notifyDataSetChanged();
                        }
                    });
                } else {
                    showDialog("要删除这条新闻吗？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(((StoreHistoryActivity) context).type.equals("store")) {
                                NewsManager.deleteFavHistory(username, data.get(position).getNewsID());
                            } else if(((StoreHistoryActivity) context).type.equals("history")) {
                                NewsManager.deleteReadHistory(username, data.get(position).getNewsID());
                            }
                            data.remove(data.get(position));
                            //((StoreHistoryActivity) context).data.remove(data.get(position));
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        return view;

    }

    public void dataClear() { data.clear(); }

    public class ViewHolder{
        TextView title;
        TextView source;
        TextView time;
        String newsID;
        ImageView image;
//        ImageView[] image_3;
        JzvdStd video;
        ImageView delete;
    }


    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

}


