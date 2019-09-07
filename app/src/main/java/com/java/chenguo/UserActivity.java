package com.java.chenguo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import com.java.chenguo.Client.ClientLogout;
import com.java.chenguo.Client.UploadData;
import com.java.chenguo.DataBase.NewsManager;
import com.java.chenguo.DataBase.UserDKKeyword;

import java.util.concurrent.TimeUnit;

public class UserActivity extends AppCompatActivity {

    private Button btn_logout;
    private TextView tv_name;
    private String username = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        btn_logout = (Button) findViewById(R.id.btn_register);
        tv_name = (TextView) findViewById(R.id.username_user);

        tv_name.setText(username);


        //设置事件响应
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("确定注销吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(UserActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                        ClientLogout.sendLogoutRequest(username);
                        username = "";
                        onBackPressed();
                    }
                });
            }
        });
    }

    //对话框
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("username", username);
        setResult(0, intent);
        finish();
    }

}
