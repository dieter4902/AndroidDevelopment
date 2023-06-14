package com.example.tracking_room;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

@Entity(tableName = "route_table")

public class Route {

    @PrimaryKey
    @NonNull
    public String name;
    public long start;
    public long end;
    public String route;
    public double distance;


    public Route(@NonNull String name, org.alternativevision.gpx.beans.Route route) {
        Gson gson = new Gson();
        this.name = name;
        this.route = gson.toJson(route);
    }

    public Route(@NonNull String name) {
        this.name = name;
    }

    public Route(@NonNull String name,long start, long end,org.alternativevision.gpx.beans.Route route, double distance) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.route = new Gson().toJson(route);
        this.distance = distance;
        Log.d("dist",distance+"");
    }


    public static org.alternativevision.gpx.beans.Route string2Route(String routeJson) {
        return new Gson().fromJson(routeJson, org.alternativevision.gpx.beans.Route.class);
    }
}
