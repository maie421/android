package com.example.myapplication;
import static com.example.myapplication.CouponItem.allArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Base64;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;
    TextView companyView;
    TextView priceView;
    TextView salePriceView;
    ImageView imageview;
    Bitmap bitmap;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        imageview = itemView.findViewById(R.id.imageView);
        bitmap = imageview.getDrawingCache();

        titleView = itemView.findViewById(R.id.titleView);
        companyView = itemView.findViewById(R.id.companyView);
        priceView = itemView.findViewById(R.id.priceView);
        salePriceView = itemView.findViewById(R.id.salePriceView);

        itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            intent.putExtra("title", titleView.getText().toString());

            view.getContext().startActivity(intent);
        });
    }

    void onBind(CouponItem item){
        titleView.setText(item.title);
        companyView.setText(item.company);
        priceView.setText(item.price);
        salePriceView.setText(item.salePrice);
        imageview.setImageBitmap(item.bitmap);
    }
    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        float scale = (float) (1024/(float)bitmap.getWidth());
        int image_w = (int) (bitmap.getWidth() * scale);
        int image_h = (int) (bitmap.getHeight() * scale);
        Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
        resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        return byteArray ;
    }
}
