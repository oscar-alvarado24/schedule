package com.colombia.eps.schedule.domain.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class CreateSchedule {
    private String workingDay;
    private List<DayOfWeek> workDays;
    private int durationAppointment;
    private String doctorName;
    private String area;
    private Map<LocalDate,Map<LocalTime,String>> appointments;

    public CreateSchedule() {
    }

    public String getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(String workingDay) {
        this.workingDay = workingDay;
    }

    public List<DayOfWeek> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(List<DayOfWeek> workDays) {
        this.workDays = workDays;
    }

    public int getDurationAppointment() {
        return durationAppointment;
    }

    public void setDurationAppointment(int durationAppointment) {
        this.durationAppointment = durationAppointment;
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

    public Map<LocalDate, Map<LocalTime, String>> getAppointments() {
        return appointments;
    }

    public void setAppointments(Map<LocalDate, Map<LocalTime, String>> appointments) {
        this.appointments = appointments;
    }
}
