package com.example.myapplication;

import static com.example.myapplication.DetailActivity.latitude;
import static com.example.myapplication.DetailActivity.longitude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String place;
    String title;

    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent(); /*데이터 수신*/
        place = intent.getExtras().getString("place");
        title = intent.getExtras().getString("title");

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


        ArrayList<String> item = getStringArrayPref("Item", title);

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //장소 찾기
        Geocoder geocoder = new Geocoder(this);
        List<Address> resultLocationList = null;
        try {
            resultLocationList = geocoder.getFromLocationName(place, 5);
            for (Address resultLocation : resultLocationList) {
                double lat = resultLocation.getLatitude();
                double lng = resultLocation.getLongitude();

                MarkerOptions markerOptionsGeocoder = new MarkerOptions();
                LatLng placeLatLng = new LatLng(lat, lng);
                markerOptionsGeocoder
                        .title(place)
                        .position(placeLatLng);
                mMap.addMarker(markerOptionsGeocoder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //현재 위치 마크
        LatLng location = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location)
                .title("현재 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(markerOptions);

        //카메라
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));


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