package com.epam.lab.exception;

public class IncorrectRequestException extends RepositoryException {

    public IncorrectRequestException() {
    }

    public IncorrectRequestException(String message) {
        super(message);
    }

    public IncorrectRequestException(Throwable cause) {
        super(cause);
    }
}
