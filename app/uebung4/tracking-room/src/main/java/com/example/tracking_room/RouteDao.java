package com.example.tracking_room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Route route);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Poi poi);

    @Query("DELETE FROM route_table")
    void deleteAll();

    @Query("SELECT * FROM route_table ORDER BY route ASC")
    LiveData<List<Route>> getAlphabetizedRoutes();

}

