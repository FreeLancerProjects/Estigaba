package com.semicolon.estigaba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TextActivity extends AppCompatActivity {

    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        tv_title = findViewById(R.id.tv_title);
        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            String title = intent.getStringExtra("title");
            tv_title.setText(title);
        }
    }
}
