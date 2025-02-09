package com.ehr.exceptions;

public class TokenExpiredException extends Exception {
    public TokenExpiredException() {
        super("The Provided OTP is expired");
    }
    
    public TokenExpiredException(String msg) {
        super(msg);
    }
}
