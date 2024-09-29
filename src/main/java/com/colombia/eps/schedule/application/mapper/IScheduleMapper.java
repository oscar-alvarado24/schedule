package com.colombia.eps.schedule.application.mapper;

import com.colombia.eps.schedule.application.dto.AppointmentSpacesRequest;
import com.colombia.eps.schedule.application.dto.AppointmentSpacesResponse;
import com.colombia.eps.schedule.application.dto.ScheduleAppointmentRequest;
import com.colombia.eps.schedule.application.dto.ScheduleRequest;
import com.colombia.eps.schedule.domain.model.AppointmentSpaces;
import com.colombia.eps.schedule.domain.model.Appointments;
import com.colombia.eps.schedule.domain.model.ScheduleAppointment;
import com.colombia.eps.schedule.domain.model.CreateSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IScheduleMapper {

    IScheduleMapper INSTANCE = Mappers.getMapper(IScheduleMapper.class);

    CreateSchedule toScheduleCreate (ScheduleRequest scheduleRequest);

    default List<DayOfWeek> toDayOfWeekList(List<String>stringDay){
        List<DayOfWeek> daysOfWeekList = new ArrayList<>();
        stringDay.forEach(day -> daysOfWeekList.add(DayOfWeek.valueOf(day.toUpperCase())));
        return daysOfWeekList;
    }

    default ScheduleAppointment toScheduleAppointment(ScheduleAppointmentRequest scheduleAppointmentRequest){
        ScheduleAppointment scheduleAppointment = new ScheduleAppointment();
        scheduleAppointment.setPatientName(scheduleAppointmentRequest.getPatientName());
        scheduleAppointment.setDoctorName(scheduleAppointmentRequest.getDoctorName());
        scheduleAppointment.setDate(scheduleAppointmentRequest.getDate().toLocalDate());
        scheduleAppointment.setTime(scheduleAppointmentRequest.getDate().toLocalTime().truncatedTo(java.time.temporal.ChronoUnit.MINUTES));
        return scheduleAppointment;
    }

    AppointmentSpaces toAppointmentRequest(AppointmentSpacesRequest appointmentSpacesRequest);

    default List<AppointmentSpacesResponse> toAppointmentResponse(List<Appointments> appointments){
        List<AppointmentSpacesResponse> appointmentSpacesResponse = new ArrayList<>();
        appointments.forEach(appointment ->{
            String doctorName = appointment.getDoctorName();
            appointment.getAppointments().forEach((key,value)->{
                AppointmentSpacesResponse spacesResponse = new AppointmentSpacesResponse();
                spacesResponse.setDoctorName(doctorName);
                spacesResponse.setTime(key);
                appointmentSpacesResponse.add(spacesResponse);
            });
        } );
        return appointmentSpacesResponse;
    }
}
