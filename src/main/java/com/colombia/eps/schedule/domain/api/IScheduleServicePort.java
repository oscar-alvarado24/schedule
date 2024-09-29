package com.colombia.eps.schedule.domain.api;

import com.colombia.eps.schedule.domain.model.AppointmentSpaces;
import com.colombia.eps.schedule.domain.model.Appointments;
import com.colombia.eps.schedule.domain.model.ScheduleAppointment;
import com.colombia.eps.schedule.domain.model.CreateSchedule;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface IScheduleServicePort {
    String createSchedule(Map<String, List<LocalDate>> holidays, Map<String, List<CreateSchedule>> schedulesRequests, YearMonth yearMonth);

    String scheduleAppointment(ScheduleAppointment scheduleAppointment);

    List<Appointments> getAppointmentSpaces(AppointmentSpaces appointmentSpaces);
}
