package com.example.myapplication;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class barcodeActivity extends AppCompatActivity {
    ImageView barCodeView;
    String title;

    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        barCodeView = findViewById(R.id.barCodeView);

        Intent intent = getIntent();

        title = intent.getExtras().getString("title");

        ArrayList<String> item = getStringArrayPref("Item", title);

        //바코드 생성
        barCodeView.setImageBitmap(textToBarCode(item.get(1)));

        titleView = findViewById(R.id.titleView);
        companyView = findViewById(R.id.companyView);
        priceView = findViewById(R.id.priceView);
        salePriceView = findViewById(R.id.salePriceView);
        imageview = findViewById(R.id.imageView);

        titleView.setText(item.get(0));
        companyView.setText(item.get(1));
        priceView.setText(item.get(2));
        salePriceView.setText(item.get(3));

        imageview.setImageBitmap(StringToBitmap(item.get(6)));
    }

    public static Bitmap textToBarCode(String data) {
        MultiFormatWriter writer = new MultiFormatWriter();


        String finaldata = Uri.encode(data, "utf-8");

        BitMatrix bm = null;
        try {
            bm = writer.encode(finaldata, BarcodeFormat.CODE_128, 300, 300);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = Bitmap.createBitmap(180, 40, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < 180; i++) {//width
            for (int j = 0; j < 40; j++) {//height
                bitmap.setPixel(i, j, bm.get(i, j) ? BLACK : WHITE);
            }
        }

        return bitmap;
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