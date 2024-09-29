package com.colombia.eps.schedule.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleAppointmentRequest {
    private String patientName;;
    private String doctorName;
    private LocalDateTime date;
}
