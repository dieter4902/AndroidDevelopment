package com.example.tracking_room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class ActivityPoiDetails extends AppCompatActivity {
    public Poi poi;
    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_single);
        Intent intent = getIntent();
        poi = new Gson().fromJson(intent.getStringExtra("poi"), Poi.class);
        Log.d("poiDetails",poi.image);
        ImageView imageView = findViewById(R.id.imageView);
        Bitmap bImage = BitmapFactory.decodeFile(poi.image);;
        imageView.setImageBitmap(bImage);
        Log.d("poidetail",imageView.getScaleX()+"   "+imageView.getScaleY());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
