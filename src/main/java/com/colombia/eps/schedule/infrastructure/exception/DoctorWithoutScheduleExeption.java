package com.colombia.eps.schedule.infrastructure.exception;

public class DoctorWithoutScheduleExeption extends RuntimeException {
    public DoctorWithoutScheduleExeption(String message) {
        super(message);
    }
}
