package com.colombia.eps.schedule.infrastructure.exception;

public class FailedToSaveAppointmentException extends RuntimeException {
    public FailedToSaveAppointmentException(String message) {
        super(message);
    }
}
