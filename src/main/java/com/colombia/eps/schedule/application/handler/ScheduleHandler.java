package com.colombia.eps.schedule.application.handler;

import com.colombia.eps.schedule.application.dto.*;
import com.colombia.eps.schedule.application.mapper.IScheduleMapper;
import com.colombia.eps.schedule.domain.api.IHolidaysServicePort;
import com.colombia.eps.schedule.domain.api.IScheduleServicePort;
import com.colombia.eps.schedule.domain.model.CreateSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleHandler implements IScheduleHandler {

    private final IHolidaysServicePort holidaysServicePort;
    private final IScheduleServicePort scheduleServicePort;
    private final IScheduleMapper scheduleMapper;
    /**
     * @param createScheduleRequest schedule for creates
     * @return List of confirmation messages by each create schedule
     */
    @Override
    public String createSchedule(CreateScheduleRequest createScheduleRequest) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-")
                .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.SHORT)
                .toFormatter(Locale.ENGLISH);

        YearMonth yearMonth = YearMonth.parse(createScheduleRequest.getYearMonth(), formatter);
        Map<String, List<CreateSchedule>> schedulesRequestByCity = createScheduleRequest.getScheduleRequests().stream()
                .collect(Collectors.groupingBy(
                        ScheduleRequest::getCity,
                        Collectors.mapping(
                                scheduleMapper::toScheduleCreate,
                                Collectors.toList()
                        )
                ));
        Set<String> cityList = schedulesRequestByCity.keySet();
        Map<String,List<LocalDate>> holidays = holidaysServicePort.getHolidays(cityList);

        return scheduleServicePort.createSchedule(holidays, schedulesRequestByCity, yearMonth);
    }

    /**
     * @param scheduleAppointmentRequest generate appointment
     * @return message confirmation of  correct process
     */
    @Override
    public String scheduleAppointment(ScheduleAppointmentRequest scheduleAppointmentRequest) {
        return scheduleServicePort.scheduleAppointment(scheduleMapper.toScheduleAppointment(scheduleAppointmentRequest));
    }

    /**
     * @param appointmentSpacesRequest Request for free spaces to make an appointment
     * @return  list of free spaces to make an appointment
     */
    @Override
    public List<AppointmentSpacesResponse> getAppointmentsSpaces(AppointmentSpacesRequest appointmentSpacesRequest) {
        return scheduleMapper.toAppointmentResponse(scheduleServicePort.getAppointmentSpaces(scheduleMapper.toAppointmentRequest(appointmentSpacesRequest)));
    }
}
