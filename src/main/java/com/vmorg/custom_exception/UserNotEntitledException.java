package com.vmorg.custom_exception;

public class UserNotEntitledException extends Exception {
    public UserNotEntitledException(String message){
        super(message);
    }
}
