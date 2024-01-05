package com.OAuth2.AuthorizationServer.exceptions;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
