package com.colombia.eps.schedule.application.handler;

import com.colombia.eps.schedule.application.dto.CreateScheduleRequest;
import com.colombia.eps.schedule.application.dto.AppointmentSpacesRequest;
import com.colombia.eps.schedule.application.dto.AppointmentSpacesResponse;
import com.colombia.eps.schedule.application.dto.ScheduleAppointmentRequest;

import java.util.List;

public interface IScheduleHandler {
    String createSchedule(CreateScheduleRequest createScheduleRequests);
    String scheduleAppointment(ScheduleAppointmentRequest scheduleAppointmentRequest);
    List<AppointmentSpacesResponse> getAppointmentsSpaces(AppointmentSpacesRequest getScheduleRequest);
}
