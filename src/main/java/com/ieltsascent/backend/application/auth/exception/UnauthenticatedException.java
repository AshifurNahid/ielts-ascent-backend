package com.ieltsascent.backend.application.auth.exception;

public class UnauthenticatedException extends AuthException {
    public UnauthenticatedException() {
        super("Unauthenticated");
    }
}
