package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyPageViewAdapter extends RecyclerView.Adapter<MyPageViewHolder>  {
    private ArrayList<CouponItem> couponList;

    //    @NonNull
//    @Override
//    public ViewHorder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.item_list, parent, false);
//
//        ViewHorder viewHorder = new ViewHorder(context, view);
//        return viewHorder;
//    }
    @NonNull
    @Override
    public MyPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_item_list, parent, false);
        return new MyPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageViewHolder holder, int position) {
        holder.onBind(couponList.get(position));
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public void setArrayList(ArrayList<CouponItem> list) {
        this.couponList = list;
    }

}
