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
        imageview = itemView.findViewById(R.id.imageView);
        titleView = itemView.findViewById(R.id.titleView);
        companyView = itemView.findViewById(R.id.companyView);
        priceView = itemView.findViewById(R.id.priceView);
        salePriceView = itemView.findViewById(R.id.salePriceView);
    }


    void onBind(CouponItem item){
        titleView.setText(item.title);
        companyView.setText(item.company);
        priceView.setText(item.price);
        salePriceView.setText(item.salePrice);
        imageview.setImageBitmap(item.bitmap);
    }
}
