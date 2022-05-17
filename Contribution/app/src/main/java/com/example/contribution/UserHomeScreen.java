package com.example.contribution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserHomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);
        setTitle("User Home Screen");
    }
}