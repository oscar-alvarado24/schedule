package com.colombia.eps.schedule.domain.spi;

import com.colombia.eps.schedule.domain.model.AppointmentSpaces;
import com.colombia.eps.schedule.domain.model.Appointments;
import com.colombia.eps.schedule.domain.model.ScheduleAppointment;
import com.colombia.eps.schedule.domain.model.CreateSchedule;

import java.time.YearMonth;
import java.util.List;

public interface ISchedulePersistencePort {
    String createSchedule(List<CreateSchedule> createSchedules, YearMonth yearMonth);

    String scheduleAppointment(ScheduleAppointment scheduleAppointment);

    List<Appointments> getAppointmentSpaces(AppointmentSpaces appointmentSpaces);
}
