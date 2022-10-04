package com.example.myapplication;
import static com.example.myapplication.CouponItem.allArrayList;
import static com.example.myapplication.CouponItem.giftArrayList;
import static com.example.myapplication.CouponItem.itemArrayList;
import static com.example.myapplication.CouponItem.myItemArrayList;
import static com.example.myapplication.CouponItem.purchaseArrayList;
import static com.example.myapplication.MainActivity.mainAdapter;
import static com.example.myapplication.MyPageActivity.adapter;
import static com.example.myapplication.MyPageActivity.mypage_type;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class MyPageViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;
    RecyclerView recyclerView;
    Button button;
    Bitmap bitmap;

    public MyPageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageview = itemView.findViewById(R.id.imageView);

        titleView = itemView.findViewById(R.id.titleView);
        companyView = itemView.findViewById(R.id.companyView);
        priceView = itemView.findViewById(R.id.priceView);
        salePriceView = itemView.findViewById(R.id.salePriceView);
        recyclerView = itemView.findViewById(R.id.recyclerView);

        button = itemView.findViewById(R.id.imageButton);

        button.setOnClickListener(view -> {
            int position = getAdapterPosition();
            int i;
            int j;
            int g;
            if (Objects.equals(mypage_type, "MY_COUPON")){
                for (i =0 ;i< allArrayList.size();i++){
                    if (Objects.equals(allArrayList.get(i).title, titleView.getText().toString())){
                        allArrayList.remove(i);
                        break;
                    }
                }
//                for (g =0 ;g< giftArrayList.size();g++){
//                    if (Objects.equals(giftArrayList.get(g).title, titleView.getText().toString())){
//                        giftArrayList.remove(g);
//                        break;
//                    }
//                }
//                for (j =0 ;j< itemArrayList.size();j++){
//                    if (Objects.equals(itemArrayList.get(j).title, titleView.getText().toString())){
//                        itemArrayList.remove(j);
//                        break;
//                    }
//                }
                myItemArrayList.remove(position);
                mainAdapter.notifyDataSetChanged();;
            }else {
                for (int p =0 ;p< purchaseArrayList.size();p++){
                    if (Objects.equals(purchaseArrayList.get(p).title, titleView.getText().toString())){
                        purchaseArrayList.remove(p);
                        break;
                    }
                }
            }
            adapter.notifyItemRemoved(position);
        });
    }

    void onBind(CouponItem item){
        titleView.setText(item.title);
        companyView.setText(item.company);
        priceView.setText(item.price);
        salePriceView.setText(item.salePrice);
        imageview.setImageBitmap(item.bitmap);
    }

}
