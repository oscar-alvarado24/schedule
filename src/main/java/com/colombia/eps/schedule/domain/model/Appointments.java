package com.colombia.eps.schedule.domain.model;

import java.time.LocalTime;
import java.util.Map;

public class Appointments {
    private String doctorName;
    private Map<LocalTime, String> appointments;

    public Appointments() {
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Map<LocalTime, String> getAppointments() {
        return appointments;
    }

    public void setAppointments(Map<LocalTime, String> appointments) {
        this.appointments = appointments;
    }
}
