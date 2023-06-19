package com.example.tracking_room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PoiDao {

    @Query("SELECT * FROM poi_table WHERE routeId=:route_id AND id=:poi_id")
    Poi loadSingle(long route_id, long poi_id);

    @Query("SELECT * FROM poi_table WHERE routeId=:route_id")
    LiveData<List<Poi>> getAll(long route_id);

    @Insert
    void insert(Poi poi);


    @Query("SELECT * FROM poi_table ORDER BY ID DESC LIMIT 1")
    long getRouteCount(); //with LiveData


}

