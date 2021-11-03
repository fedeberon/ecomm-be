package com.ideaas.ecomm.ecomm.exception;

public class LoginTicketException extends RuntimeException {
    public LoginTicketException(String message) {
        super(message);
    }

    public LoginTicketException(String message, Throwable cause) {
        super(message, cause);
    }
}
