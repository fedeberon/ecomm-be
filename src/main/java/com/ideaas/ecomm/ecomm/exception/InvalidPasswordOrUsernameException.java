package com.ideaas.ecomm.ecomm.exception;

public class InvalidPasswordOrUsernameException  extends RuntimeException {

    public InvalidPasswordOrUsernameException(final String message) {
        super(message);
    }
}
