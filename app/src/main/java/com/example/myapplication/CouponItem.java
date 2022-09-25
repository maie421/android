package com.example.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class CouponItem {
    public String title;
    public String company;
    public String price;
    public String salePrice;
    public Bitmap bitmap;
    public static ArrayList<CouponItem> itemArrayList;

    public CouponItem(String title, String company, String price, String salePrice, Bitmap bitmap){
        this.title = title;
        this.company= company;
        this.price = price;
        this.salePrice = salePrice;
        this.bitmap = bitmap;
    }
}
