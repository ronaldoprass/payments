package com.rprass.payment.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long seriaLVersionUID = 1L;

    public ResourceNotFoundException(String ex) {
        super(ex);
    }
}
