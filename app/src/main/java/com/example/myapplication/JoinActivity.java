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

import java.util.Objects;

public class JoinActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText passwordTmp;
    private SharedPreferences preferences;
    private SharedPreferences preferences_auto;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordTmp = findViewById(R.id.passwordTmp);

        Button joinBtn = findViewById(R.id.joinBtn);

        preferences = getSharedPreferences("User", MODE_PRIVATE);
        preferences_auto = getSharedPreferences("AutoUser", MODE_PRIVATE);

        Intent intentExtras = getIntent();

        id = intentExtras.getExtras().getString("id");

        if (!Objects.equals(id, "")){
            email.setText(id);
            email.setEnabled(false);
        }

        joinBtn.setOnClickListener(view -> {
            if (email.getText().toString().replace(" ", "").equals("")) {
                Toast.makeText(getApplicationContext(), "아이디 칸이 핀간입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.getText().toString().equals(passwordTmp.getText().toString())) {
                Toast.makeText(getApplicationContext(), "비밀번호를 재입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(email.getText().toString(), password.getText().toString());
            editor.commit();

            SharedPreferences.Editor editor_auto = preferences_auto.edit();
            editor_auto.putString("login_id", email.getText().toString());
            editor_auto.commit();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAffinity();
            intent.putExtra("id", email.getText().toString()); /*송신*/
            startActivity(intent);
        });
    }
}