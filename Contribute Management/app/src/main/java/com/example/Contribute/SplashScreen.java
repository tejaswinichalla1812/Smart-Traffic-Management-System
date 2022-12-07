package com.example.Contribute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    Button userPage,goodWillPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        userPage=findViewById(R.id.userPage_iv);
        goodWillPage=findViewById(R.id.goodwillPage_iv);

        userPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserHomeScreen.class);
                startActivity(intent);

            }
        });

        goodWillPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GoodWillLogin.class);
                startActivity(intent);
            }
        });

    }
}