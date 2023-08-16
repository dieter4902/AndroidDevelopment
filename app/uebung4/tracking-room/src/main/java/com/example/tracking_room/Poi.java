package com.example.tracking_room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "poi_table")

public class Poi {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long routeId;
    public String location;
    public String coords;
    public String description;

    public String image;


    public Poi(long routeId,String location, String coords, String description, String image) {
        this.routeId = routeId;
        this.location = location;
        this.coords = coords;
        this.description = description;
        this.image = image;
    }
}
