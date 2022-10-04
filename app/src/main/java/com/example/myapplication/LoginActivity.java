package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    private SharedPreferences preferences;
    private SharedPreferences preferences_auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Button loginBtn = findViewById(R.id.login);

        preferences = getSharedPreferences("User", MODE_PRIVATE);
        preferences_auto = getSharedPreferences("AutoUser", MODE_PRIVATE);

        loginBtn.setOnClickListener(view -> {
            if (email.getText().toString().replace(" ", "").equals("")) {
                Toast.makeText(getApplicationContext(), "아이디 칸이 핀간입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.getText().toString().replace(" ", "").equals("")) {
                Toast.makeText(getApplicationContext(), "비밀번호를 재입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!preferences.getString(email.getText().toString(), "").equals(password.getText().toString())) {
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = preferences_auto.edit();

            editor.putString("login_id",email.getText().toString());
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAffinity();
            intent.putExtra("id", email.getText().toString()); /*송신*/
            startActivity(intent);
        });
    }
}