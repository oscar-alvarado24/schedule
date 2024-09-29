package com.colombia.eps.schedule.infrastructure.input.rest;

import com.colombia.eps.schedule.application.dto.AppointmentSpacesRequest;
import com.colombia.eps.schedule.application.dto.AppointmentSpacesResponse;
import com.colombia.eps.schedule.application.dto.CreateScheduleRequest;
import com.colombia.eps.schedule.application.dto.ScheduleAppointmentRequest;
import com.colombia.eps.schedule.application.handler.IScheduleHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final IScheduleHandler scheduleHandler;

    @Operation(summary = "create schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "schedule created successfully", content = @Content),
            @ApiResponse(responseCode = "500", description = "Fail process for create schedule", content = @Content)
    })
    @PostMapping("/create-schedule")
    public String createSchedule(@RequestBody CreateScheduleRequest createScheduleRequests){
        return scheduleHandler.createSchedule(createScheduleRequests);
    }

    @Operation(summary = "schedule appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "schedule appointment successfully", content = @Content),
            @ApiResponse(responseCode = "500", description = "Fail process for schedule appointment", content = @Content)
    })
    @PostMapping("/schedule-appointment")
    public String scheduleAppointment(@RequestBody ScheduleAppointmentRequest scheduleAppointmentRequest){
        return scheduleHandler.scheduleAppointment(scheduleAppointmentRequest);
    }

    @Operation(summary = "create schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "schedule created successfully", content = @Content),
            @ApiResponse(responseCode = "500", description = "Fail process for create schedule", content = @Content)
    })
    @GetMapping("/get-appointments")
    public List<AppointmentSpacesResponse> getSchedule(@RequestParam String doctorName,
                                                       @RequestParam String area,
                                                       @RequestParam String date) {

        AppointmentSpacesRequest appointmentSpacesRequest = new AppointmentSpacesRequest(doctorName, area, date);
        return scheduleHandler.getAppointmentsSpaces(appointmentSpacesRequest);
    }

}
