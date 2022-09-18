package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        Button loginBtn = findViewById(R.id.login);

        loginBtn.setOnClickListener(view -> {
            if (email.getText().toString().replace(" ", "").equals("")){
                Toast.makeText(getApplicationContext(), "아이디 칸이 핀간입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.getText().toString().replace(" ", "").equals("")){
                Toast.makeText(getApplicationContext(), "비밀번호를 재입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAffinity();
            intent.putExtra("id",email.getText().toString()); /*송신*/
            startActivity(intent);
        });
    }
}