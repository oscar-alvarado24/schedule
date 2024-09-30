package com.colombia.eps.schedule.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleAppointment {
    private String patientName;
    private String doctorName;
    private LocalDate date;
    private LocalTime time;

    public ScheduleAppointment() {
        //constructor for generate a ScheduleAppointment object empty
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
