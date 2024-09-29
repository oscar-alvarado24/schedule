package com.colombia.eps.schedule.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateScheduleRequest {

    private String yearMonth;
    private List<ScheduleRequest> scheduleRequests;
}