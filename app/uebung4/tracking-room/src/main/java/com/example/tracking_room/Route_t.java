package com.example.tracking_room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "route_table")

public class Route_t {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "route")
    private String mRoute;

    public Route_t(@NonNull String route) {
        this.mRoute = route;
    }

    public String getRoute() {
        return this.mRoute;
    }
}
