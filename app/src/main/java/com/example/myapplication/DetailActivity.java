package com.example.myapplication;

import static com.example.myapplication.CouponItem.myItemArrayList;
import static com.example.myapplication.CouponItem.purchaseArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String id;
    String title;
    String company;
    String price;
    String salePrice;

    byte[] byteArray;
    Bitmap bitmap;

    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;
    Button purchaseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent;
        init(); //객체 정의

    }
    private void init() {
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        Intent intent = getIntent(); /*데이터 수신*/

        id = intent.getExtras().getString("id");
        title = intent.getExtras().getString("title");
        company = intent.getExtras().getString("company");
        price = intent.getExtras().getString("price");
        salePrice = intent.getExtras().getString("salePrice");
        byteArray = intent.getExtras().getByteArray("image");

        titleView = findViewById(R.id.title);
        companyView = findViewById(R.id.company);
        priceView = findViewById(R.id.price);
        salePriceView = findViewById(R.id.salePrice);
        imageview = findViewById(R.id.imageView);
        purchaseBtn = findViewById(R.id.purchase);

        titleView.setText(title);
        companyView.setText(company);
        priceView.setText(price);
        salePriceView.setText(salePrice);

        if (byteArray != null ){
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageview.setImageBitmap(bitmap);
        }

        purchaseBtn.setOnClickListener(view -> {
//            purchaseArrayList.add(0,new CouponItem(title, company, price, salePrice, bitmap));
            finish();
        });
    }



}