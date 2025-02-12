package com.ehr.exceptions;

public class EntityAlreadyExistsException extends Exception{

    public EntityAlreadyExistsException() {
        super("User already exists with same mail id");
    }

    public EntityAlreadyExistsException(String msg) {
        super(msg);
    }

}
