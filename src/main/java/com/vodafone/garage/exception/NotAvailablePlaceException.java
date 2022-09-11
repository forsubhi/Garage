package com.vodafone.garage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
public class NotAvailablePlaceException  extends RuntimeException{
    public NotAvailablePlaceException(String message) {
        super(message);
    }
}
