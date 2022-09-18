package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView title;
    TextView company;
    TextView price;
    TextView salePrice;
    Button submitBtn;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        init(); //객체 정의
        SettingListener(); //리스너 등록
        ClickBtn();

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.add);
    }


    private void init() {
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        title = findViewById(R.id.title);
        company = findViewById(R.id.company);
        price = findViewById(R.id.price);
        salePrice = findViewById(R.id.salePrice);
        submitBtn = findViewById(R.id.submit);

        Intent intent = getIntent(); /*데이터 수신*/

        id = intent.getExtras().getString("id");
    }

    private void ClickBtn(){
        submitBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("title", title.getText().toString());
            intent.putExtra("company", company.getText().toString());
            intent.putExtra("price", price.getText().toString());
            intent.putExtra("salePrice", salePrice.getText().toString());
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new ProductActivity.TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.home: {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("id",id); /*송신*/;
                    intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                }
                case R.id.menu_search: {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    intent.putExtra("id",id); /*송신*/
                    startActivity(intent);
                    return true;
                }
                case R.id.add: {
                    return true;
                }
                case R.id.my_page: {
                    Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                    intent.putExtra("id",id); /*송신*/
                    startActivity(intent);
                    return true;
                }
            }

            return false;
        }
    }
}