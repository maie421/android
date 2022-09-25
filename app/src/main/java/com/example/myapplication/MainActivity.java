package com.example.myapplication;

import static com.example.myapplication.CouponItem.itemArrayList;
import static com.example.myapplication.CouponItem.giftArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    View searchView;
    String id;
    String title;
    String company;
    String price;
    String salePrice;
    Bitmap bitmap;
    Bitmap bm;

    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;
    ImageView banner;
    Button allButon;
    Button martButon;

    RecyclerView recyclerView;
    GridViewAdapter adapter;

    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); //객체 정의
        SettingListener(); //리스너 등록

        searchView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        });
        //버튼
        martButon.setOnClickListener(view -> {
            adapter.setArrayList(giftArrayList);
            adapter.notifyDataSetChanged();
        });
        //버튼
        allButon.setOnClickListener(view -> {
            adapter.setArrayList(itemArrayList);
            adapter.notifyDataSetChanged();
        });

//        imageview.setOnClickListener(view -> {
//            Log.d("테222스트","ㅁㅇㄴㄹㅇㄹ");
//            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
//            intent.putExtra("id",id);
//            intent.putExtra("title", titleView.getText().toString());
//            intent.putExtra("company", companyView.getText().toString());
//            intent.putExtra("price", priceView.getText().toString());
//            intent.putExtra("salePrice", salePriceView.getText().toString());
//            intent.putExtra("image", byteArray);
//
//            startActivity(intent);
//        });
    }


    private void init(){
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        searchView = findViewById(R.id.searchView);
        imageview = findViewById(R.id.imageView);
        titleView = findViewById(R.id.title);
        companyView = findViewById(R.id.company);
        priceView = findViewById(R.id.price);
        salePriceView = findViewById(R.id.salePrice);
        banner = findViewById(R.id.banner);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        allButon = findViewById(R.id.all_button);
        martButon = findViewById(R.id.mart_button);

        adapter = new GridViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        //이미지
        AssetManager am = getResources().getAssets() ;
        InputStream delivery = null;
        try {
            delivery = am.open("배민.jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        bm = BitmapFactory.decodeStream(delivery) ;

        //쿠폰 데이터 세팅
        CouponItem.insertItemArrayList(bm);
        CouponItem.insertgiftArrayList(bm);

        adapter.setArrayList(itemArrayList);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.home: {
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
                    startActivityForResult(intent,0);
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

    //화면에 표시되기 직전
    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        banner.setImageResource(R.drawable.banner2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            title = data.getExtras().getString("title");
            company = data.getExtras().getString("company");
            price = data.getExtras().getString("price");
            salePrice = data.getExtras().getString("salePrice");

            byteArray = data.getExtras().getByteArray("image");
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            titleView = findViewById(R.id.title);
            companyView = findViewById(R.id.company);
            priceView = findViewById(R.id.price);
            salePriceView = findViewById(R.id.salePrice);
            imageview = findViewById(R.id.imageView);

            titleView.setText(title);
            companyView.setText(company);
            priceView.setText(price);
            salePriceView.setText(salePrice);
            imageview.setImageBitmap(bitmap);
        }
    }
}