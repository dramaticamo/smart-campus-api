package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

public class Sensor {

    private String id;
    private String type;
    private String roomId;

    // ✅ NEW: readings list
    private List<SensorReading> readings = new ArrayList<>();

    public Sensor() {}

    public Sensor(String id, String type, String roomId) {
        this.id = id;
        this.type = type;
        this.roomId = roomId;
    }

    // getters & setters
    public String getId() { 
        return id; 
    }

    public void setId(String id) { 
        this.id = id; 
    }

    public String getType() { 
        return type; 
    }

    public void setType(String type) { 
        this.type = type; 
    }

    public String getRoomId() { 
        return roomId; 
    }

    public void setRoomId(String roomId) { 
        this.roomId = roomId; 
    }

    // ✅ NEW: readings getter
    public List<SensorReading> getReadings() {
        return readings;
    }

    // (optional but good)
    public void setReadings(List<SensorReading> readings) {
        this.readings = readings;
    }
}