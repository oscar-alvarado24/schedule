package com.colombia.eps.schedule.infrastructure.exception;

public class DynamoDbManagerException extends RuntimeException {
    public DynamoDbManagerException(String message) {
        super(message);
    }
}
