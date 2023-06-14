package com.example.tracking_room;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RouteWithPois {

    @Embedded public Route route;
    @Relation(
            parentColumn = "name",
            entityColumn = "location"
    )
    public List<Poi> pois;
}
