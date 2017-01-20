package com.example.androidjokes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_library);
        String joke = this.getIntent().getExtras().getString("joke");
        TextView jokeTextView = (TextView)findViewById(R.id.joke_text_view);
        jokeTextView.setText(joke);
    }
}
