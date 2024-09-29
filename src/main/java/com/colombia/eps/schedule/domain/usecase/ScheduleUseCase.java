package com.colombia.eps.schedule.domain.usecase;

import com.colombia.eps.schedule.common.util.Constants;
import com.colombia.eps.schedule.domain.api.IScheduleServicePort;
import com.colombia.eps.schedule.domain.model.AppointmentSpaces;
import com.colombia.eps.schedule.domain.model.Appointments;
import com.colombia.eps.schedule.domain.model.ScheduleAppointment;
import com.colombia.eps.schedule.domain.model.CreateSchedule;
import com.colombia.eps.schedule.domain.spi.ISchedulePersistencePort;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleUseCase implements IScheduleServicePort {

    private final ISchedulePersistencePort schedulePersistencePort;

    public ScheduleUseCase(ISchedulePersistencePort schedulePersistencePort) {
        this.schedulePersistencePort = schedulePersistencePort;
    }


    /**
     * @param holidays          list of holidays that not work
     * @param schedulesRequests request of schedules for create
     * @param yearMonth         month and year to which the schedule to be created belong
     * @return message of confirmation that the creation schedule is correct
     */
    @Override
    public String createSchedule(Map<String, List<LocalDate>> holidays, Map<String, List<CreateSchedule>> schedulesRequests, YearMonth yearMonth) {
        List<CreateSchedule> createSchedules = Collections.synchronizedList(new ArrayList<>());
        schedulesRequests.forEach((city, scheduleCreateList) -> {
            scheduleCreateList
                    .parallelStream()
                    .forEach(scheduleCreate -> {
                        scheduleCreate.setAppointments(createAppointmentToSet(scheduleCreate, holidays.get(city), yearMonth));
                        createSchedules.add(scheduleCreate);
                    });
        });
        return schedulePersistencePort.createSchedule(createSchedules, yearMonth);
    }

    /**
     * @param scheduleAppointment appointment scheduling request
     * @return message confirmation
     */
    @Override
    public String scheduleAppointment(ScheduleAppointment scheduleAppointment) {
        return schedulePersistencePort.scheduleAppointment(scheduleAppointment);
    }

    /**
     * @param appointmentSpaces dates that conform the request for appointment spaces
     * @return list of appointment spaces
     */
    @Override
    public List<Appointments> getAppointmentSpaces(AppointmentSpaces appointmentSpaces) {
        return schedulePersistencePort.getAppointmentSpaces(appointmentSpaces);
    }

    private List<LocalDate> getDaysOfMonth(YearMonth yearMonth, LocalDate firstDayOfMonth, List<DayOfWeek> workDays, List<LocalDate> holidays) {
        List<LocalDate> daysOfMonth = new ArrayList<>();


        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate currentDate = firstDayOfMonth.plusDays(day - 1);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (workDays.contains(dayOfWeek) && !holidays.contains(currentDate)) {
                daysOfMonth.add(currentDate);
            }
        }

        return daysOfMonth;
    }

    private Map<LocalTime, String> generateAppointments(LocalTime startTime, LocalTime endTime, int durationAppointment, boolean allDay) {

        Map<LocalTime, String> appointmentSpaces = new LinkedHashMap<>();
        int timeBreak = allDay ? 2 : 3;
        LocalTime breakTime = startTime.plusHours(timeBreak);
        LocalTime breakfastTime = LocalTime.of(12, 0);

        while (!startTime.isAfter(endTime)) {
            int increment = 0;
            if (startTime.equals(breakTime) || (startTime.equals(breakfastTime) && allDay)) {
                increment = startTime.equals(breakfastTime) ? 120 : 15;
            } else {
                appointmentSpaces.put(startTime, Constants.AVAILABLE);
                increment = durationAppointment;
            }
            startTime = startTime.plusMinutes(increment);
        }
        return appointmentSpaces;
    }

    private Map<LocalTime, String> createAppointmentSpaces(int durationAppointment, String workingDay) {
        int startHour = 0;
        int startMinute = 0;
        int endHour = 0;
        int endMinute = 0;
        boolean allDay = false;
        switch (workingDay) {
            case Constants.MORNING:
                startHour = 6;
                startMinute = 30;
                break;
            case Constants.AFTERNOON:
                startHour = 1;
                startMinute = 20;
                break;
            default:
                startHour = 8;
                endHour = 6;
                allDay = true;
        }
        LocalTime startDay = LocalTime.of(startHour, startMinute);
        LocalTime endDay = allDay ? LocalTime.of(endHour, endMinute) : startDay.plusMinutes(375);
        return generateAppointments(startDay, endDay, durationAppointment, allDay);
    }

    private Map<LocalDate, Map<LocalTime, String>> createAppointmentToSet(CreateSchedule createSchedule, List<LocalDate> holidays, YearMonth yearMonth) {
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        List<LocalDate> dayOfMonths = getDaysOfMonth(yearMonth, firstDayOfMonth, createSchedule.getWorkDays(), holidays);
        Map<LocalTime, String> appointmentSpaces = createAppointmentSpaces(createSchedule.getDurationAppointment(), createSchedule.getWorkingDay());
        return dayOfMonths.stream()
                .collect(Collectors.toMap(
                        dayOfMonth -> dayOfMonth,
                        dayOfMonth -> appointmentSpaces
                ));
    }
}
