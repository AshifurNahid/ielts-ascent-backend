package com.ieltsascent.backend.application.auth.exception;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException() {
        super("Email already registered");
    }
}
