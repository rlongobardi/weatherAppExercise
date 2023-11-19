package com.myweather.api.exceptions;

public class ApiCallException extends RuntimeException {
    public ApiCallException(String message, Exception ex) {
        super(message);
    }
}