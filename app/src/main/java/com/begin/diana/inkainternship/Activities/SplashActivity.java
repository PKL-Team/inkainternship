package com.begin.diana.inkainternship.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.activityCobaCoba;

public class SplashActivity extends AppCompatActivity {
    private static int splashInterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent home = new Intent(SplashActivity.this, activityCobaCoba.class);
                startActivity(home);
                finish();
            }
        }, splashInterval);
    };

}
