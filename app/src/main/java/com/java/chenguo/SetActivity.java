package com.java.chenguo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.java.chenguo.DataBase.NewsManager;

public class SetActivity extends AppCompatActivity {

    Toolbar toolbar;
    String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        toolbar = (Toolbar) findViewById(R.id.set_tool_bar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        }
        actionBar.setTitle("设置");
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);

        PrefsFragment fg = new PrefsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        fg.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.layout_set_content, fg).commit();
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        private SwitchPreference night_pre;
        private Preference clear_hot;
        private Preference clear_dkhot;
        private String username;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.set_preference);

            Bundle bundle = getArguments();
            username = bundle.getString("username");

            night_pre = (SwitchPreference) findPreference("night_mod");
            clear_hot = (Preference) findPreference("clear_hot");
            clear_dkhot = (Preference) findPreference("clear_dkhot");

            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                night_pre.setChecked(false);
            } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                night_pre.setChecked(true);
            } else {
                night_pre.setChecked(false);
            }


            night_pre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if(night_pre.isChecked()) {
                        Log.d("test","进入更改为夜间");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        //night_pre.setChecked(true);
                    }
                    else {
                        Log.d("test","进入更改为日间");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        //night_pre.setChecked(false);
                    }
                    return false;
                }
            });

            clear_hot.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("确定要清除喜好词吗？");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Log.d("test", "username:" + username);
                            NewsManager.clearUserKeywords(username);
                            Toast.makeText(getContext(),  "清除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("取消", null);
                    builder.create().show();

                    return false;
                }
            });

            clear_dkhot.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("确定要清除屏蔽词吗？");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NewsManager.clearUserDKKeywords(username);
                            Toast.makeText(getContext(),  "清除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("取消", null);
                    builder.create().show();


                    return false;
                }
            });
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootkey) {

        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //对话框
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialogTitle);
        builder.setNegativeButton("确定", onClickListener);
        builder.setPositiveButton("取消", null);
        builder.create().show();
    }

}
