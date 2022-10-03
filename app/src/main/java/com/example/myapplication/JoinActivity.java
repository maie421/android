package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText passwordTmp;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordTmp = findViewById(R.id.passwordTmp);

        Button joinBtn = findViewById(R.id.joinBtn);

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        joinBtn.setOnClickListener(view -> {
            if (email.getText().toString().replace(" ", "").equals("")){
                Toast.makeText(getApplicationContext(), "아이디 칸이 핀간입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.getText().toString().equals(passwordTmp.getText().toString())){
                Toast.makeText(getApplicationContext(), "비밀번호를 재입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAffinity();
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(email.getText().toString(), password.getText().toString());
            editor.commit();

            intent.putExtra("id",email.getText().toString()); /*송신*/
            startActivity(intent);
        });
    }
}