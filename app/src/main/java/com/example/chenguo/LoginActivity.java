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

public class LoginActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    private CheckBox cb_password;
    private CheckBox cb_login;
    private ImageView iv_seeps;

    private String username = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        et_name = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        cb_login = (CheckBox) findViewById(R.id.checkBox_login);
        cb_password = (CheckBox) findViewById(R.id.checkBox_password);
        iv_seeps = (ImageView) findViewById(R.id.iv_see_password);
        iv_seeps.setSelected(false);
        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        //设置事件响应
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存一下用户名
                login();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        cb_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    cb_login.setChecked(false);
                }
            }
        });
        cb_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    cb_password.setChecked(true);
                }
            }
        });
        iv_seeps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswordVisible();
            }
        });

        //initData
        //判断是否记住密码
        setTextName();
        if (rememberPassword()) {
            cb_password.setChecked(true);//勾选记住密码
            setPassword();//把密码输入到输入框中
        }

        //判断是否自动登录
        if (autoLogin()) {
            cb_login.setChecked(true);
            login();

        }
    }

    /**
     * 是否记住密码
     * */

    private boolean rememberPassword() {
        return false;
    }

    /**
     * 加载用户名
     * */

    private void setTextName() {
        et_name.setText("SigmaLethe");
    }

    /**
     * 加载密码
     * */

    private void setPassword() {
        et_password.setText("123456");
    }

    /**
     * 是否自动登录
     * */

    private boolean autoLogin() {
        return false;
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
     * 设置密码可见性
     * */
    private void setPasswordVisible() {
        //Log.d("test", "进来了");
        if(iv_seeps.isSelected()) {
            //Log.d("test", "，之前应当是不可见的");
            iv_seeps.setSelected(false);
            iv_seeps.setImageResource(R.drawable.ic_visibility);
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        else {
            //Log.d("test", "，之前应当是可见的");
            iv_seeps.setSelected(true);
            iv_seeps.setImageResource(R.drawable.ic_visibility_off);
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    /**
     * 登录函数
     * */
    private void login() {
        //检查账号和密码是否空
        if(getAccount().isEmpty()) {
            Toast.makeText(this, "输入账号为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(getPassword().isEmpty()) {
            Toast.makeText(this, "输入密码为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        //showLoading();
        btn_login.setClickable(false);

        ClientLogin.sendLoginRequest("1", getAccount(), getPassword());
        String ret = ClientLogin.responseType;
        Log.d("test", "return:" + ret);

        if (ret.equals("2")) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            username = getAccount();
            onBackPressed();
        }
        else {
            Toast.makeText(this, "输入的账号或密码不正确", Toast.LENGTH_SHORT).show();
            btn_login.setClickable(true);
            //hideLoading();
        }


    }

    /**
     * 返回用户名
     * */

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("username", username);
        setResult(0, intent);
        finish();
    }
}