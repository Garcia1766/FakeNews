package com.example.chenguo;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import com.example.chenguo.Client.ClientLogin;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_password;
    private EditText et_password_2;
    private Button btn_register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        btn_register = (Button) findViewById(R.id.btn_register);
        et_name = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_2 = (EditText) findViewById(R.id.et_password_2);
        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_password_2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        //设置事件响应
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    /**
     * 获取账号
     * */
    private String getAccount() {
        return et_name.getText().toString().trim();
    }

    /**
     * 获取密码
     * */
    private String getPassword() {
        return et_password.getText().toString().trim();
    }

    /**
     * 获取密码
     * */
    private String getPassword_2() {
        return et_password_2.getText().toString().trim();
    }

    /**
     * 登录函数
     * */
    private void register() {
        //检查账号和密码是否空，以及密码是否匹配
        if(getAccount().isEmpty()) {
            Toast.makeText(this, "输入账号为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(getPassword().isEmpty()) {
            Toast.makeText(this, "输入密码为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!getPassword().equals(getPassword_2())){
            Toast.makeText(this, "两次输入的密码不匹配", Toast.LENGTH_SHORT).show();
            return;
        }

        //showLoading();
        btn_register.setClickable(false);

        ClientLogin.sendLoginRequest("0", getAccount(), getPassword());
        String ret = ClientLogin.responseType;
        Log.d("test", "return:" + ret);

        if (ret.equals("1")) {  //这里条件改一下
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (ret.equals("0")) {
            Toast.makeText(this, "账号已存在，请换一个用户名>_<", Toast.LENGTH_SHORT).show();
            btn_register.setClickable(true);
            //hideLoading();
        }


    }
}