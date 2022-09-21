package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ProductActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView title;
    TextView company;
    TextView price;
    TextView salePrice;
    Button submitBtn;
    ImageView imageView;
    Bitmap bitmap;
    float scale;
    String id;
    int image_w;
    int image_h;
    Bitmap resize;
    byte[] byteArray;
    Bitmap img;

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
            intent.putExtra("id",id);
            intent.putExtra("title", title.getText().toString());
            intent.putExtra("company", company.getText().toString());
            intent.putExtra("price", price.getText().toString());
            intent.putExtra("salePrice", salePrice.getText().toString());
            intent.putExtra("image", byteArray);

            setResult(RESULT_OK,intent);
            finish();
        });

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
//
//
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//            scale = (float) (1024/(float)bitmap.getWidth());
//            image_w = (int) (bitmap.getWidth() * scale);
//            image_h = (int) (bitmap.getHeight() * scale);
//            resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
//            resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byteArray = stream.toByteArray();
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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("id",id); /*송신*/;
                    startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
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
}