package com.vmorg.custom_exception;

public class MachineNotCreatedException extends Exception{
    MachineNotCreatedException(String message){
        super(message);
    }
}
