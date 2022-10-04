package com.example.myapplication;

import static com.example.myapplication.CouponItem.allArrayList;
import static com.example.myapplication.CouponItem.giftArrayList;
import static com.example.myapplication.CouponItem.itemArrayList;
import static com.example.myapplication.CouponItem.myItemArrayList;
import static com.example.myapplication.CouponItem.purchaseArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView title;
    TextView company;
    TextView price;
    TextView salePrice;
    Button submitBtn;
    ImageView imageView;
    String id;
    Bitmap img;

    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    public SharedPreferences preferences;

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
        imageView = findViewById(R.id.img);

        arrayList = new ArrayList<>();
        arrayList.add("상품");
        arrayList.add("쿠폰");
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Intent intent = getIntent(); /*데이터 수신*/

        id = intent.getExtras().getString("id");
    }

    private void ClickBtn(){
        submitBtn.setOnClickListener(view -> {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            float scale = (float) (1024/(float)bitmap.getWidth());
            int image_w = (int) (bitmap.getWidth() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            ArrayList<String> data = new ArrayList<>();
            ArrayList<String> item_data = new ArrayList<>();

            data.add(title.getText().toString());
            data.add(company.getText().toString());
            data.add(price.getText().toString());
            data.add(salePrice.getText().toString());
            data.add(spinner.getSelectedItem().toString());
            data.add(id);
            data.add(BitmapToString(bitmap));
            setStringArrayPref("Item",  title.getText().toString(), data);

            intent.putExtra("id",id);
            intent.putExtra("title", title.getText().toString());
            intent.putExtra("company", company.getText().toString());
            intent.putExtra("price", price.getText().toString());
            intent.putExtra("salePrice", salePrice.getText().toString());
            intent.putExtra("type", spinner.getSelectedItem().toString());
            intent.putExtra("image", byteArray);

            allArrayList.add(0,new CouponItem(title.getText().toString(), company.getText().toString(), price.getText().toString(), salePrice.getText().toString(),spinner.getSelectedItem().toString(), id,bitmap));

            setResult(RESULT_OK,intent);
            finish();
        });

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
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
                    finish();
                    return true;
                }
                case R.id.menu_search: {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    intent.putExtra("id",id); /*송신*/
                    startActivity(intent);
                    return true;
                }
                case R.id.add: {
                    return true;
                }
                case R.id.my_page: {
                    finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.add);
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
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
}