package com.example.evoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(()->{
            startActivity(new Intent(getApplicationContext(),LogIn.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            finish();
        },3700);
    }
}