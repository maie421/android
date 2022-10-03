package com.example.myapplication;

import static com.example.myapplication.CouponItem.allArrayList;
import static com.example.myapplication.CouponItem.itemArrayList;
import static com.example.myapplication.CouponItem.giftArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public SharedPreferences preferences;

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
        martButton.setOnClickListener(view -> {
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
            mainAdapter.setArrayList(itemArrayList);
            mainType = "ITEM";
            mainAdapter.notifyDataSetChanged();
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
        try {
            gift = am.open("배민.jpeg");
            item = am.open("coffee.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        giftBm = BitmapFactory.decodeStream(gift) ;
        itemBm = BitmapFactory.decodeStream(item) ;

        //쿠폰 데이터 세팅
        CouponItem.insertItemArrayList(itemBm);
        CouponItem.insertgiftArrayList(giftBm);

        preferences = getSharedPreferences("Item", MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            CouponItem.insertItemArrayList(getStringArrayPref("Item",entry.getKey()));
        }

        mainAdapter.setArrayList(allArrayList);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
//
//        ArrayList<String> data = new ArrayList<>();
//        ArrayList<String> item_data = new ArrayList<>();
//
//        data.add("카페아메리카노 Tall");
//        data.add("스타벅스");
//        data.add("4100");
//        data.add("3500");
//        data.add("상품권");
//        data.add(id);
//        data.add(BitmapToString(giftBm));
//
//        item_data.add("5000원 쿠폰");
//        item_data.add("배달의 민족");
//        item_data.add("5000");
//        item_data.add("2000");
//        item_data.add("쿠폰");
//        item_data.add(id);
//        item_data.add(BitmapToString(itemBm));
//        for (int i = 0; i < 10; i++) {
//            setStringArrayPref("Item", "카페아메리카노 Tall"+ i , data);
//            setStringArrayPref("Item", "5000원 쿠폰"+ i , item_data);
//
//
//        }
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
            type = data.getExtras().getString("type");

            byteArray = data.getExtras().getByteArray("image");
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


            if (Objects.equals(mainType, "ALL") || Objects.equals(mainType, type)){
                mainAdapter.notifyItemInserted(0);
            }
        }
    }

    public void setStringArrayPref(String context, String key, ArrayList<String> values) {
        preferences = getSharedPreferences(context, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
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