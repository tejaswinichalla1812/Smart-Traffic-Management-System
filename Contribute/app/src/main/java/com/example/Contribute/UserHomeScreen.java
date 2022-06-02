package com.example.Contribute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserHomeScreen extends AppCompatActivity {

    Button loginPage,registerPage,aboutusPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);

        loginPage=findViewById(R.id.loginPage_iv);
        registerPage=findViewById(R.id.registerPage_iv);
        aboutusPage=findViewById(R.id.aboutusPage_iv);

        aboutusPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), About.class);
                startActivity(intent);
            }
        });

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Logup.class);
                startActivity(intent);
            }
        });

    }
}
