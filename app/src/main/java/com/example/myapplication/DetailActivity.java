package com.example.myapplication;

import static com.example.myapplication.CouponItem.StringToBitmap;
import static com.example.myapplication.CouponItem.insertPurchaseArrayList;
import static com.example.myapplication.CouponItem.myItemArrayList;
import static com.example.myapplication.CouponItem.purchaseArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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

    public static SharedPreferences preferences_purchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent;
        init(); //객체 정의
    }

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

        titleView.setText(item.get(0));
        companyView.setText(item.get(1));
        priceView.setText(item.get(2));
        salePriceView.setText(item.get(3));

        if (item.get(6) != null) {
            imageview.setImageBitmap(StringToBitmap(item.get(6)));
        }

        purchaseBtn.setOnClickListener(view -> {

            ArrayList<String> data = new ArrayList<>();

            data.add(item.get(0));
            data.add(item.get(1));
            data.add(item.get(2));
            data.add(item.get(3));
            data.add(item.get(4));
            data.add(item.get(5));
            data.add(item.get(6));

            setStringArrayPref("Purchase", item.get(0), data);
            insertPurchaseArrayList(item);
            finish();
        });
    }

    public ArrayList<String> getStringArrayPref(String context, String key) {
        preferences_purchase = getSharedPreferences(context, MODE_PRIVATE);
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
        preferences_purchase = getSharedPreferences(context, MODE_PRIVATE);
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