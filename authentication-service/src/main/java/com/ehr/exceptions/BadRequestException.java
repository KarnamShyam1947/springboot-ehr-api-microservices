package com.ehr.exceptions;

public class BadRequestException extends Exception {
    public BadRequestException() {
        super("Bad Request");
    }
   
    public BadRequestException(String msg) {
        super(msg);
    }
}
