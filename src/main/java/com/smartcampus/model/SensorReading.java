package com.smartcampus.model;

// represents a single reading from a sensor
// for example temperature or CO2 value at a specific time
public class SensorReading {

    private String id;      // unique reading ID
    private double value;   // measured value
    private long timestamp; // time of reading (epoch time)

    // default constructor (needed for JSON)
    public SensorReading() {}

    // constructor to create a reading with all fields
    public SensorReading(String id, double value, long timestamp) {
        this.id = id;
        this.value = value;
        this.timestamp = timestamp;
    }

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}