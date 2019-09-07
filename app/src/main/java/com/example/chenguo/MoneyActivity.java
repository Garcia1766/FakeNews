package com.example.chenguo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MoneyActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);

        TextView text_content = (TextView) findViewById(R.id.txt_content);
        text_content.setText("别看了，你没有钱");
    }
}
