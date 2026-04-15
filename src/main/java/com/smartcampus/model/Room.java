package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

// represents a room in the smart campus system
public class Room {

    private String id;              // unique room ID
    private String name;            // room name
    private int capacity;           // max number of people
    private List<String> sensorIds = new ArrayList<>(); // sensors linked to this room

    // default constructor (needed for JSON)
    public Room() {}

    // constructor to create room with basic info
    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // getters and setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<String> getSensorIds() { return sensorIds; }
    public void setSensorIds(List<String> sensorIds) { this.sensorIds = sensorIds; }
}