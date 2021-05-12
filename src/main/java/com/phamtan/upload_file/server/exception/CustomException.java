package com.phamtan.upload_file.server.exception;

public class CustomException extends RuntimeException {

    private String name;
    private String message;
    private Throwable cause;

    public CustomException(String name, String message, Throwable throwable) {
        this.name = name;
        this.message = message;
        this.cause = throwable;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
