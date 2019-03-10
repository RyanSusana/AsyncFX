package com.ryansusana.asyncfx;

public class AsyncException extends RuntimeException {

    public AsyncException(String message) {
        super(message);
    }

    public AsyncException(String message, InterruptedException e) {
        super(message, e);
    }
}
