package com.colombia.eps.schedule.infrastructure.exception;

public class DoctorWithoutAppointmentSpacesExeption extends RuntimeException {
    public DoctorWithoutAppointmentSpacesExeption(String message) {
        super(message);
    }
}
