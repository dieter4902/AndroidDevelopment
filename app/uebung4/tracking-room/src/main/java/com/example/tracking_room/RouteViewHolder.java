package com.example.tracking_room;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {//https://stackoverflow.com/questions/24471109/recyclerview-onclick
    private final TextView routeItemView;
    private Route route;


    private RouteViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        routeItemView = itemView.findViewById(R.id.textView);
    }

    public void bind(String text, Route route) {
        routeItemView.setText(text);
        this.route = route;
    }

    static RouteViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onClick(View view) {
        Log.d("route",route.name);
        MainActivity.main.startActivity(route);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}


