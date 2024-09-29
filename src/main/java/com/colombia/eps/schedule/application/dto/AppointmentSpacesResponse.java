package com.colombia.eps.schedule.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSpacesResponse {
    private String doctorName;
    private LocalTime time;
}
