package com.example.myapplication;

import static com.example.myapplication.CouponItem.allArrayList;
import static com.example.myapplication.CouponItem.getMyArrayList;
import static com.example.myapplication.CouponItem.getPurchaseArrayList;
import static com.example.myapplication.CouponItem.myItemArrayList;
import static com.example.myapplication.CouponItem.purchaseAllArrayList;
import static com.example.myapplication.CouponItem.purchaseArrayList;
import static com.example.myapplication.CouponItem.useCouponArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.sdk.user.UserApiClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

public class MyPageActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView saleView;
    TextView purchaseView;
    TextView useCouponButton;
    Button signoutButton;
    private SharedPreferences preferences_auto;
    private SharedPreferences preferences;

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
        saleView = findViewById(R.id.forSaleView);
        purchaseView = findViewById(R.id.purchaseView);
        signoutButton = findViewById(R.id.logoutButton);
        useCouponButton = findViewById(R.id.useCoupon);

        preferences = getSharedPreferences("User", MODE_PRIVATE);
        preferences_auto = getSharedPreferences("AutoUser", MODE_PRIVATE);

        Intent intent = getIntent(); /*데이터 수신*/

        id = intent.getExtras().getString("id");

        TextView title = findViewById(R.id.title);
        title.setText(id + " 주인님");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new MyPageViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        getPurchaseItemList();
        getPurchaseArrayList(id);
        adapter.setArrayList(purchaseArrayList);

        //판매큐폰
        saleView.setOnClickListener(view -> {
            getMyArrayList(id);
            adapter.setArrayList(myItemArrayList);
            mypage_type = "MY_COUPON";
            adapter.notifyDataSetChanged();

            //클릭색 변경
            saleView.setTextColor(Color.rgb(200,0,0));
            purchaseView.setTextColor(Color.rgb(0,0,0));
            useCouponButton.setTextColor(Color.rgb(0,0,0));
        });

        //구매쿠폰
        purchaseView.setOnClickListener(view -> {
            getPurchaseArrayList(id);
            adapter.setArrayList(purchaseArrayList);
            mypage_type = "PURCHASE";
            adapter.notifyDataSetChanged();

            //클릭색 변경
            saleView.setTextColor(Color.rgb(0,0,0));
            purchaseView.setTextColor(Color.rgb(200,0,0));
            useCouponButton.setTextColor(Color.rgb(0,0,0));
        });

        //사용쿠폰
        useCouponButton.setOnClickListener(view -> {
            adapter.setArrayList(useCouponArrayList);
            mypage_type = "USE_COUPON";
            adapter.notifyDataSetChanged();
            //클릭색 변경
            saleView.setTextColor(Color.rgb(0,0,0));
            purchaseView.setTextColor(Color.rgb(0,0,0));
            useCouponButton.setTextColor(Color.rgb(200,0,0));
        });

        signoutButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor_auto = preferences_auto.edit();
            SharedPreferences.Editor editor = preferences.edit();

            editor_auto.remove("login_id");
            editor.remove(id);
            editor_auto.commit();
            editor.commit();

            UserApiClient.getInstance().unlink(error -> {return null;});

            Intent intent_init = new Intent(getApplicationContext(), InitActivity.class);
            finishAffinity();
            startActivity(intent_init);
        });
    }

    private void getPurchaseItemList() {
        purchaseAllArrayList.clear();
        preferences = getSharedPreferences("Purchase", MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            CouponItem.insertPurchaseArrayList(getStringArrayPref("Purchase",entry.getKey()));
        }

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
        purchaseView.setTextColor(Color.rgb(200,0,0));
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
}