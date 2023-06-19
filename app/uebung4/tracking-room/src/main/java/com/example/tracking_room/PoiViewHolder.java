package com.example.tracking_room;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class PoiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {//https://stackoverflow.com/questions/24471109/recyclerview-onclick
    private final TextView poiItemView;
    private Poi poi;


    private PoiViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        poiItemView = itemView.findViewById(R.id.textView);
    }

    public void bind(String text, Poi poi) {
        poiItemView.setText(text);
        this.poi = poi;
    }

    static PoiViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new PoiViewHolder(view);
    }

    @Override
    public void onClick(View view) {
        Log.d("poi",poi.coords);
        //MainActivity.main.startActivity(poi);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}


