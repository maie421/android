package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
//        imageview = imageview.findViewById(R.id.imageView);
        titleView = (TextView)  itemView.findViewById(R.id.titleView);
        companyView = (TextView) itemView.findViewById(R.id.companyView);
        priceView = (TextView) itemView.findViewById(R.id.priceView);
        salePriceView = (TextView) itemView.findViewById(R.id.salePriceView);
    }


    void onBind(CouponItem item){
        Log.d("sdfㅇ","ddd");
        titleView.setText(item.title);
        companyView.setText(item.company);
        priceView.setText(item.price);
        salePriceView.setText(item.salePrice);
//        imageview.setImageBitmap(item.bitmap);
    }
}
