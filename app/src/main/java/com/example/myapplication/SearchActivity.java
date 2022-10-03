package com.example.myapplication;

import static com.example.myapplication.CouponItem.allArrayList;
import static com.example.myapplication.CouponItem.itemArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    SearchView searchView;
    String id;
    RecyclerView recyclerView;
    ViewAdapter adapter;
    public ArrayList<CouponItem> filterArrayList = new ArrayList<>();
//    ArrayList<CouponItem> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init(); //객체 정의
        SettingListener(); //리스너 등록
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        searchView = findViewById(R.id.searchView);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new ViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        Intent intent = getIntent(); /*데이터 수신*/
        id = intent.getExtras().getString("id");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterArrayList.clear();
                for (int i =0 ; i < allArrayList.size(); i++){
                    if (allArrayList.get(i).title.contains(s)){
                        insertFilterArrayList(allArrayList.get(i).title, allArrayList.get(i).company,allArrayList.get(i).price, allArrayList.get(i).salePrice,allArrayList.get(i).type,allArrayList.get(i).member_idx,allArrayList.get(i).bitmap);
                    }

                }
                adapter.setArrayList(filterArrayList);
                return false;
            }
        });
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new SearchActivity.TabSelectedListener());
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
                    return true;
                }
                case R.id.add: {
                    Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                    intent.putExtra("id",id); /*송신*/
                    startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.menu_search);
        adapter.setArrayList(allArrayList);
    }
    public void insertFilterArrayList(String title, String company, String price, String salePrice, String type, String member_idx, Bitmap bitmap) {
        filterArrayList.add(new CouponItem(title, company,price, salePrice,type,member_idx,bitmap));
    }
}