package com.smartcampus.exception;

// custom exception used when a sensor is not available
// for example, when sensor status is MAINTENANCE
public class SensorUnavailableException extends RuntimeException {

    public SensorUnavailableException(String message) {
        super(message); // pass message to parent class
    }
}