package com.colombia.eps.schedule.domain.model;

import java.time.LocalTime;
import java.util.Map;

public class Appointments {
    private String doctorName;
    private Map<LocalTime, String> doctorAppointments;

    public Appointments() {
        //constructor for generate a Appointments object empty
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Map<LocalTime, String> getDoctorAppointments() {
        return doctorAppointments;
    }

    public void setDoctorAppointments(Map<LocalTime, String> doctorAppointments) {
        this.doctorAppointments = doctorAppointments;
    }
}
