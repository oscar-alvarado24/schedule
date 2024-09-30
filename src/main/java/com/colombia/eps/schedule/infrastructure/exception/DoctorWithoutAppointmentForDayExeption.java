package com.colombia.eps.schedule.infrastructure.exception;

public class DoctorWithoutAppointmentForDayExeption extends RuntimeException {
    public DoctorWithoutAppointmentForDayExeption(String message) {
        super(message);
    }
}
