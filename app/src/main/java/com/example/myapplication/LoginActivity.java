package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Button loginBtn = findViewById(R.id.login);

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        loginBtn.setOnClickListener(view -> {
            if (email.getText().toString().replace(" ", "").equals("")) {
                Toast.makeText(getApplicationContext(), "아이디 칸이 핀간입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.getText().toString().replace(" ", "").equals("")) {
                Toast.makeText(getApplicationContext(), "비밀번호를 재입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!preferences.getString("user_id", "").equals(email.getText().toString()) ||
                    !preferences.getString("user_pwd", "").equals(password.getText().toString())) {
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAffinity();
            intent.putExtra("id", email.getText().toString()); /*송신*/
            startActivity(intent);
        });
    }
}