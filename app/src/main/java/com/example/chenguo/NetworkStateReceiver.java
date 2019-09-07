package com.example.chenguo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

public class NetworkStateReceiver extends BroadcastReceiver {

    public boolean isConnected;
    private Toast toast;
    private CountDownTimer cdt;

    @Override
    public void onReceive (Context context, Intent intent) {
        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            isConnected = true;
//            if(context.getClass().toString().equals("class com.example.chenguo.MainActivity") ||
//                    context.getClass().toString().equals("class com.example.chenguo.SearchActivity")) {
//                toast = Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT);
//                toast.show();
//                cdt = new CountDownTimer(1000, 1000) {
//                    @Override
//                    public void onTick(long l) {
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        toast.cancel();
//                    }
//                };
//                cdt.start();
//            }
        } else {
            isConnected = false;
//            if(context.getClass().toString().equals("class com.example.chenguo.MainActivity") ||
//                    context.getClass().toString().equals("class com.example.chenguo.SearchActivity")) {
//                toast = Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT);
//                toast.show();
//                cdt = new CountDownTimer(1000, 1000) {
//                    @Override
//                    public void onTick(long l) {
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        toast.cancel();
//                    }
//                };
//                cdt.start();
//            }
        }
    }

}
