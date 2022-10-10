package com.example.myapplication;

import static com.example.myapplication.CouponItem.purchaseArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class InitActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        Button joinBtn = findViewById(R.id.join);
        joinBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        });


        Button loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        preferences = getSharedPreferences("AutoUser", MODE_PRIVATE);
        if (preferences.getString("login_id", null) != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAffinity();
            intent.putExtra("id", preferences.getString("login_id", null)); /*송신*/
            startActivity(intent);
            return;
        }


    }
}