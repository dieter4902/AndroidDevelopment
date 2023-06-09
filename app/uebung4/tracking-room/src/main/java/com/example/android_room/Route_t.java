package com.example.android_room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.alternativevision.gpx.beans.Route;

@Entity(tableName = "route_table")

public class Route_t {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "route")

    private String name;
    private String start;
    private String end;
    private String duration;
    private String pois;
    private Route gpx_route;

    public Route_t(@NonNull String word) {
        //this.mWord = word;
    }

    public String getWord() {
        //return this.mWord;
        return "";
    }
}
