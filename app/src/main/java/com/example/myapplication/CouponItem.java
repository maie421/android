package com.example.myapplication;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class CouponItem {
    public String title;
    public String company;
    public String price;
    public String salePrice;
    public Bitmap bitmap;
    public String type;
    public String member_idx;
    public String datetime;
    public static ArrayList<CouponItem> itemArrayList = new ArrayList<>();
    public static ArrayList<CouponItem> giftArrayList = new ArrayList<>();
    public static ArrayList<CouponItem> allArrayList = new ArrayList<>();
    public static ArrayList<CouponItem> purchaseArrayList = new ArrayList<>();
    public static ArrayList<CouponItem> purchaseAllArrayList = new ArrayList<>();
    public static ArrayList<CouponItem> myItemArrayList = new ArrayList<>();
    public static ArrayList<CouponItem> useCouponArrayList = new ArrayList<>();

    public CouponItem(String title, String company, String price, String salePrice, String type, String member_idx, Bitmap bitmap, String datetime) {
        this.title = title;
        this.company = company;
        this.price = price;
        this.salePrice = salePrice;
        this.bitmap = bitmap;
        this.type = type;
        this.member_idx = member_idx;
        this.datetime = datetime;
    }

    public static void getGiftArrayList() {
        giftArrayList.clear();
        for (int i =0 ;i< allArrayList.size();i++){
            if (Objects.equals(allArrayList.get(i).type, "쿠폰")){
                giftArrayList.add(new CouponItem(allArrayList.get(i).title, allArrayList.get(i).company,allArrayList.get(i).price,allArrayList.get(i).salePrice,allArrayList.get(i).type,allArrayList.get(i).member_idx,allArrayList.get(i).bitmap, allArrayList.get(i).datetime));
            }
        }
    }

    public static void getItemArrayList() {
        itemArrayList.clear();
        for (int i =0 ;i< allArrayList.size();i++){
            if (Objects.equals(allArrayList.get(i).type, "상품")){
                itemArrayList.add(new CouponItem(allArrayList.get(i).title, allArrayList.get(i).company,allArrayList.get(i).price,allArrayList.get(i).salePrice,allArrayList.get(i).type,allArrayList.get(i).member_idx,allArrayList.get(i).bitmap,allArrayList.get(i).datetime));
            }
        }
    }

    public static void getPurchaseArrayList(String member_idx) {
        purchaseArrayList.clear();
        for (int i =0 ;i< purchaseAllArrayList.size();i++){
            if (Objects.equals(purchaseAllArrayList.get(i).member_idx, member_idx)){
                purchaseArrayList.add(new CouponItem(purchaseAllArrayList.get(i).title, purchaseAllArrayList.get(i).company,purchaseAllArrayList.get(i).price,purchaseAllArrayList.get(i).salePrice,purchaseAllArrayList.get(i).type,purchaseAllArrayList.get(i).member_idx,purchaseAllArrayList.get(i).bitmap,purchaseAllArrayList.get(i).datetime));
            }
        }
    }

    public static void getMyArrayList(String member_idx) {
        myItemArrayList.clear();
        for (int i =0 ;i< allArrayList.size();i++){
            if (Objects.equals(allArrayList.get(i).member_idx, member_idx)){
                myItemArrayList.add(new CouponItem(allArrayList.get(i).title, allArrayList.get(i).company,allArrayList.get(i).price,allArrayList.get(i).salePrice,allArrayList.get(i).type,allArrayList.get(i).member_idx,allArrayList.get(i).bitmap, allArrayList.get(i).datetime));
            }
        }
    }

    public static void insertItemArrayList(ArrayList<String> item) {
        allArrayList.add(new CouponItem(item.get(0), item.get(1), item.get(2), item.get(3),item.get(4),item.get(5),StringToBitmap(item.get(6)),item.get(7)));
    }

    public static void insertPurchaseArrayList(ArrayList<String> item) {
        purchaseAllArrayList.add(new CouponItem(item.get(0), item.get(1), item.get(2), item.get(3),item.get(4),item.get(5),StringToBitmap(item.get(6)),item.get(7)));
    }

    public static void insertUseItemArrayList(ArrayList<String> item) {
        useCouponArrayList.add(new CouponItem(item.get(0), item.get(1), item.get(2), item.get(3),item.get(4),item.get(5),StringToBitmap(item.get(6)),item.get(7)));
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
