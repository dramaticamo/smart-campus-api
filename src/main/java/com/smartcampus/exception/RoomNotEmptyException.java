package com.smartcampus.exception;

// custom exception for when trying to delete a room that still has sensors
public class RoomNotEmptyException extends RuntimeException {

    public RoomNotEmptyException(String message) {
        super(message); // pass message to parent class
    }
}