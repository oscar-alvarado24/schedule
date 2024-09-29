package com.colombia.eps.schedule.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
    private String city;
    private String workingDay;
    private String area;
    private List<String> workDays;
    private int durationAppointment;
    private String doctorName;
}
