package com.ieltsascent.backend.application.common.exception;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
