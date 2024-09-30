package com.colombia.eps.schedule.domain.model;

import java.time.LocalDate;

public class AppointmentSpaces {
    private String doctorName;
    private String area;
    private LocalDate date;

    public AppointmentSpaces() {
        //constructor for generate a AppointmentSpaces object empty
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
