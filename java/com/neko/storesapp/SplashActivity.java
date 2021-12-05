package com.neko.storesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3400;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user != null){
            Intent lPanel = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(lPanel);
            finish();
        }

        ImageView whitePoint = findViewById(R.id.whitePoint);
        TextView splashText = findViewById(R.id.splashText);
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottieSplash);

        splashText.animate().translationX(1400).setDuration(1000).setStartDelay(3000);
        whitePoint.animate().scaleX(1000).scaleY(1000).setDuration(700).setStartDelay(1000);
        //lottieAnimationView.animate().scaleY(2).scaleX(2);
        //lottieAnimationView.animate().translationX(-1400).setDuration(1000).setStartDelay(3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent lPanel = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(lPanel);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}