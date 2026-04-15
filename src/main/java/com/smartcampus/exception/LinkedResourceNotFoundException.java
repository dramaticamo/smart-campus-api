package com.smartcampus.exception;

// custom exception used when a related resource does not exist
// for example, creating a sensor with a roomId that is not found
public class LinkedResourceNotFoundException extends RuntimeException {

    public LinkedResourceNotFoundException(String message) {
        super(message); // pass message to parent class
    }
}