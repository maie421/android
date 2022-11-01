package com.example.myapplication;

import static com.example.myapplication.CouponItem.getPurchaseArrayList;
import static com.example.myapplication.CouponItem.insertPurchaseArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String id;
    String title;

    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;
    Button purchaseBtn;
    Button mapBtn;
    TextView dateView;
    String dateTmp;
    int sec;
    Handler handler = new Handler();

    private SharedPreferences preferences;

    static double longitude;
    static double latitude;

    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init(); //객체 정의
    }
    //쿠폰 마감시간
    private void dateTimeHandler() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dateFormat(sec--);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        title = intent.getExtras().getString("title");

        ArrayList<String> item = getStringArrayPref("Item", title);

        titleView = findViewById(R.id.title);
        companyView = findViewById(R.id.company);
        priceView = findViewById(R.id.price);
        salePriceView = findViewById(R.id.salePrice);
        imageview = findViewById(R.id.imageView);
        purchaseBtn = findViewById(R.id.purchase);
        mapBtn = findViewById(R.id.map);
        dateView = findViewById(R.id.date);
        titleView.setText(item.get(0));
        companyView.setText(item.get(1));
        priceView.setText(item.get(2));
        salePriceView.setText(item.get(3));
        dateView.setText(item.get(7));

        dateTmp = (String) dateView.getText();

        if (item.get(6) != null) {
            imageview.setImageBitmap(StringToBitmap(item.get(6)));
        }

        purchaseBtn.setOnClickListener(view -> {
            preferences = getSharedPreferences("AutoUser", MODE_PRIVATE);
            ArrayList<String> data = new ArrayList<>();
            id = preferences.getString("login_id", null);
            data.add(item.get(0));
            data.add(item.get(1));
            data.add(item.get(2));
            data.add(item.get(3));
            data.add(item.get(4));
            data.add(id);
            data.add(item.get(6));
            data.add(item.get(7));

            setStringArrayPref("Purchase", item.get(0), data);
            insertPurchaseArrayList(item);
            getPurchaseArrayList(id);
            finish();
        });

        mapBtn.setOnClickListener(view -> {
            //위치 권한 체크
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DetailActivity.this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }

            //현재 위치
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            longitude = location.getLongitude();//위도
                            latitude = location.getLatitude();//경도
                        }
                    });

            Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
            mapIntent.putExtra("id",id); /*송신*/
            mapIntent.putExtra("place",item.get(1)); /*송신*/
            mapIntent.putExtra("title",item.get(0)); /*송신*/
            startActivity(mapIntent);
        });

        String[] date = item.get(7).split("-");
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]),23,59,59);

        sec = (int) Duration.between(endDate,startDate).getSeconds()*-1;
        if (ChronoUnit.DAYS.between(startDate, endDate) == 0){
            dateTimeHandler();

        }

    }

    private void dateFormat(int sec) {
        int min = sec / 60;
        int hour = min / 60;
        sec = sec % 60;
        min = min % 60;
        dateView.setText( dateTmp + String.format(" %d:%d:%d", hour, min, sec));
    }

    public ArrayList<String> getStringArrayPref(String context, String key) {
        SharedPreferences preferences_purchase = getSharedPreferences(context, MODE_PRIVATE);
        String json = preferences_purchase.getString(key, null);

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

    public void setStringArrayPref(String context, String key, ArrayList<String> values) {
        SharedPreferences preferences_purchase = getSharedPreferences(context, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences_purchase.edit();
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

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}