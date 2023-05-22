package com.example.gps_tracker;

import java.time.LocalDateTime;

class Node {
    LocalDateTime time;
    double latitude;
    double longitude;
    double height;

    public Node(LocalDateTime time, double latitude, double longitude, double height) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
    }

    public String toStringArr() {
        return time + "," + latitude + "," + longitude + "," + height + "\n";
    }
}