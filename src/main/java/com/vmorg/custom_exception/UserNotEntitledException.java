package com.vmorg.custom_exception;

public class UserNotEntitledException extends Exception {
    UserNotEntitledException(String message){
        super(message);
    }
}
