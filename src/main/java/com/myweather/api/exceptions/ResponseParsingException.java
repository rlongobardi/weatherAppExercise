package com.myweather.api.exceptions;

public class ResponseParsingException extends RuntimeException {
    public ResponseParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}