package com.campus.shuttle.service.exception;

public class InvalidTripException extends RuntimeException {
    public InvalidTripException(String message) {
        super(message);
    }
}

