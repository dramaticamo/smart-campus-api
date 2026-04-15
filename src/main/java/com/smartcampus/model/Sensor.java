package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

// represents a sensor in the system (e.g. temperature, CO2)
public class Sensor {

    private String id;        // unique sensor ID
    private String type;      // type of sensor (Temperature, CO2, etc.)
    private String roomId;    // which room this sensor belongs to
    private String status;    // ACTIVE, MAINTENANCE, OFFLINE

    private double currentValue; // latest reading value

    // get current sensor value
    public double getCurrentValue() {
        return currentValue;
    }

    // update current sensor value
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    // list of all readings for this sensor
    private List<SensorReading> readings = new ArrayList<>();

    // default constructor (needed for JSON)
    public Sensor() {}

    // constructor for basic sensor info
    public Sensor(String id, String type, String roomId) {
        this.id = id;
        this.type = type;
        this.roomId = roomId;
    }

    // get sensor status
    public String getStatus() {
        return status;
    }

    // set sensor status (ACTIVE / MAINTENANCE / OFFLINE)
    public void setStatus(String status) {
        this.status = status;
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

    // get all readings for this sensor
    public List<SensorReading> getReadings() {
        return readings;
    }

    // set readings list (optional)
    public void setReadings(List<SensorReading> readings) {
        this.readings = readings;
    }
}