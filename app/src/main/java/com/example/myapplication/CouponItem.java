package com.example.myapplication;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CouponItem {
    public String title;
    public String company;
    public String price;
    public String salePrice;
    public Bitmap bitmap;
    public static ArrayList<CouponItem> itemArrayList;
    public static ArrayList<CouponItem> giftArrayList;

    public CouponItem(String title, String company, String price, String salePrice, Bitmap bitmap){
        this.title = title;
        this.company= company;
        this.price = price;
        this.salePrice = salePrice;
        this.bitmap = bitmap;
    }
    public static void insertItemArrayList(Bitmap bm){
        itemArrayList = new ArrayList<>();
        for (int i =0; i<40; i++){
            itemArrayList.add(new CouponItem("5000원 쿠폰","배달의 민족","5000","2000",bm));
        }
    }

    public static void insertgiftArrayList(Bitmap bm){
        giftArrayList = new ArrayList<>();
        for (int i =0; i < 40; i++){
            giftArrayList.add(new CouponItem("마트쿠폰","배달의 민족","5000","2000",bm));
        }
    }
}
