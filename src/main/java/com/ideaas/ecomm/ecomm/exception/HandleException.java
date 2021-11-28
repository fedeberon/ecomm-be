package com.ideaas.ecomm.ecomm.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class HandleException {

    @ExceptionHandler(value = LoginTicketException.class )
    protected ResponseEntity handleConflict(RuntimeException ex, WebRequest request) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(value = AfipException.class )
    protected ResponseEntity handleAfipException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

}
