package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init(); //객체 정의
        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.menu_search);
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new SearchActivity.TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.home: {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.add: {
                    Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                    startActivity(intent);
//                    finish();
                    return true;
                }
                case R.id.my_page: {
                    Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                    startActivity(intent);
                    return true;
                }
            }

            return false;
        }
    }
}