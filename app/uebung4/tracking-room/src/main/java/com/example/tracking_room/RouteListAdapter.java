package com.example.tracking_room;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class RouteListAdapter extends ListAdapter<Route_t, RouteViewHolder> {
    public RouteListAdapter(@NonNull DiffUtil.ItemCallback<Route_t> diffCallback) {
        super(diffCallback);
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RouteViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        Route_t current = getItem(position);
        holder.bind(current.getRoute());
    }

    static class RouteDiff extends DiffUtil.ItemCallback<Route_t> {

        @Override
        public boolean areItemsTheSame(@NonNull Route_t oldItem, @NonNull Route_t newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Route_t oldItem, @NonNull Route_t newItem) {
            return oldItem.getRoute().equals(newItem.getRoute());
        }
    }
}

