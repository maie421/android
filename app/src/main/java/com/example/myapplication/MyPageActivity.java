package com.example.myapplication;

import static com.example.myapplication.CouponItem.allArrayList;
import static com.example.myapplication.CouponItem.myItemArrayList;
import static com.example.myapplication.CouponItem.purchaseArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyPageActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView forSaleView;
    TextView purchaseView;
    public static String mypage_type = "PURCHASE";
    String id;
    RecyclerView recyclerView;
    public static MyPageViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        init(); //객체 정의
        SettingListener(); //리스너 등록
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        forSaleView = findViewById(R.id.forSaleView);
        purchaseView = findViewById(R.id.purchaseView);

        Intent intent = getIntent(); /*데이터 수신*/

        id = intent.getExtras().getString("id");

        TextView title = findViewById(R.id.title);
        title.setText(id + " 주인님");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new MyPageViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        forSaleView.setOnClickListener(view -> {
            adapter.setArrayList(myItemArrayList);
            mypage_type = "MY_COUPON";
            adapter.notifyDataSetChanged();
        });
        purchaseView.setOnClickListener(view -> {
            adapter.setArrayList(purchaseArrayList);
            mypage_type = "PURCHASE";
            adapter.notifyDataSetChanged();
        });
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new MyPageActivity.TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.home: {
                    finish();
                    return true;
                }
                case R.id.menu_search: {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    intent.putExtra("id",id); /*송신*/
                    startActivity(intent);
                    return true;
                }
                case R.id.add: {
                    Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                    intent.putExtra("id",id); /*송신*/
                    startActivity(intent);
                    return true;
                }
                case R.id.my_page: {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.my_page);
        adapter.setArrayList(purchaseArrayList);
    }

}