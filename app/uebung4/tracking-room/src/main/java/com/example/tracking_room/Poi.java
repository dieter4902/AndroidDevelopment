package com.example.tracking_room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "poi_table")

public class Poi {

    @PrimaryKey
    @NonNull
    public String location;
    public String coords;
    public String description;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public String image;


    public Poi(@NonNull String location, String coords, String description, String image) {
        this.location = location;
        this.coords = coords;
        this.description = description;
        this.image = image;
    }
}
