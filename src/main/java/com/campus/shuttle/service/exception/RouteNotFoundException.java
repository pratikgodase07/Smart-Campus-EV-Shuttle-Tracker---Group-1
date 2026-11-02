package com.campus.shuttle.service.exception;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(String message) {
        super(message);
    }
}

