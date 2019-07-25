package com.example.cipso.ui.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cipso.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_MS = 200;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        runnable = () -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                UserListingActivity.startActivity(SplashActivity.this);
            } else {
                LoginActivity.startIntent(SplashActivity.this);
            }
            finish();
        };

        handler.postDelayed(runnable, SPLASH_TIME_MS);
    }
}
