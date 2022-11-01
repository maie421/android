package com.example.myapplication;

import static com.example.myapplication.CouponItem.allArrayList;
import static com.example.myapplication.CouponItem.itemArrayList;
import static com.example.myapplication.CouponItem.giftArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    View searchView;
    String id;
    String title;
    String company;
    String price;
    String salePrice;
    String type;
    String mainType = "ALL";
    Bitmap bitmap;
    Bitmap itemBm;
    Bitmap giftBm;
    Bitmap gift1Bm;

    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;
    ImageView banner;
    Button allButton;
    Button martButton;
    Button itemButton;

    RecyclerView recyclerView;
    public static GridViewAdapter mainAdapter;
    public static SharedPreferences preferences_purchase;
    public static SharedPreferences preferences;
    public static SharedPreferences preferences_use_item;

    Handler handler = new Handler();

    int i = 0;

    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); //객체 정의
        SettingListener(); //리스너 등록

        searchView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        });
        //버튼
        martButton.setOnClickListener(view -> {
            CouponItem.getGiftArrayList();
            mainAdapter.setArrayList(giftArrayList);
            mainType = "GIFT";
            mainAdapter.notifyDataSetChanged();
        });
        allButton.setOnClickListener(view -> {
            mainAdapter.setArrayList(allArrayList);
            mainType = "ALL";
            mainAdapter.notifyDataSetChanged();
        });
        itemButton.setOnClickListener(view -> {
            CouponItem.getItemArrayList();
            mainAdapter.setArrayList(itemArrayList);
            mainType = "ITEM";
            mainAdapter.notifyDataSetChanged();
        });
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
        allButton = findViewById(R.id.all_button);
        martButton = findViewById(R.id.mart_button);
        itemButton = findViewById(R.id.item_button);

        mainAdapter = new GridViewAdapter();
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        //이미지
        AssetManager am = getResources().getAssets() ;
        InputStream gift = null;
        InputStream item = null;
        InputStream gift1 = null;
        try {
            gift = am.open("배민.png");
            item = am.open("coffee.png");
            gift1 = am.open("교촌_치킨.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        giftBm = BitmapFactory.decodeStream(gift) ;
        itemBm = BitmapFactory.decodeStream(item) ;
        gift1Bm = BitmapFactory.decodeStream(gift1) ;

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
//
//        for (int i = 0; i < 10; i++) {
//            ArrayList<String> data = new ArrayList<>();
//            ArrayList<String> item_data = new ArrayList<>();
//
//            data.add("카페아메리카노 Tall"+ i);
//            data.add("스타벅스 낙성대DT점");
//            data.add("4100");
//            data.add("3500");
//            data.add("상품");
//            data.add(id);
//            data.add(BitmapToString(itemBm));
//            data.add("2022-10-28");
//
//            item_data.add("5000원 쿠폰"+ i);
//            item_data.add("배달의 민족");
//            item_data.add("배달의 민족");
//            item_data.add("5000");
//            item_data.add("2000");
//            item_data.add("쿠폰");
//            item_data.add(id);
//            item_data.add(BitmapToString(giftBm));
//            item_data.add("2022-10-28");
//
//            setStringArrayPref("Item", "카페아메리카노 Tall"+ i , data);
//            setStringArrayPref("Item", "5000원 쿠폰"+ i , item_data);
//
//
//        }

        preferences = getSharedPreferences("Item", MODE_PRIVATE);
        preferences_purchase = getSharedPreferences("Purchase", MODE_PRIVATE);
        preferences_use_item = getSharedPreferences("UseItem", MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        Map<String,?> use_item_keys = preferences_use_item.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            CouponItem.insertItemArrayList(getStringArrayPref("Item",entry.getKey()));
        }
        for(Map.Entry<String,?> use_item_entry : use_item_keys.entrySet()){
            CouponItem.insertUseItemArrayList(getStringArrayPref("Item",use_item_entry.getKey()));
        }
        //쿠폰 데이터 세팅
        CouponItem.getItemArrayList();
        CouponItem.getGiftArrayList();

        mainAdapter.setArrayList(allArrayList);



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
        bottomNavigationView.setSelectedItemId(R.id.home);
        super.onStart();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (i == 0){
                                banner.setImageResource(R.drawable.banner);
                            }
                            if (i == 1){
                                banner.setImageResource(R.drawable.banner2);
                            }
                            if (i == 2){
                                banner.setImageResource(R.drawable.banner3);
                            }
                            if (i == 3){
                                banner.setImageResource(R.drawable.banner4);
                            }
                        }
                    });

                    try {
                        Thread.sleep(4000);
                        i++;
                        if (i == 4){
                            i = 0;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            title = data.getExtras().getString("title");
            company = data.getExtras().getString("company");
            price = data.getExtras().getString("price");
            salePrice = data.getExtras().getString("salePrice");
            type = data.getExtras().getString("type");

            byteArray = data.getExtras().getByteArray("image");
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


            if (Objects.equals(mainType, "ALL") || Objects.equals(mainType, type)){
                mainAdapter.notifyItemInserted(0);
            }
        }
    }

    public ArrayList<String> getStringArrayPref(String context, String key) {
        preferences = getSharedPreferences(context, MODE_PRIVATE);
        String json = preferences.getString(key, null);

        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }
}