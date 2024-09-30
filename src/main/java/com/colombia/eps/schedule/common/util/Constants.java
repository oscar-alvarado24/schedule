package com.colombia.eps.schedule.common.util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String AVAILABLE = "disponible";
    public static final String MORNING = "mañana";
    public static final String AFTERNOON = "tarde";
    public static final String DATE_INDEX = "date_index";
    public static final String AREA_INDEX = "area_index";
    public static final String SAVE_SCHEDULE_MONTH_SUCCESSFULLY = "Se crean las agendas correctamente";
    public static final String SAVE_SCHEDULE_MONTH_FAILED = "Fallo el proceso de guardado de la lista de agendas";
    public static final String DYNAMO_ROL = "DYNAMO_ROL";
    public static final String ROLE_SESSION_NAME_DYNAMO = "dynamo-conn";
    public static final String SCHEDULE = "schedule-";
    public static final String CREATE_SCHEDULES_MONTH_FAILED = "Fallo el proceso de creacion de las agendas";
    public static final String TABLE_EXIST = "The table {} already exists.";
    public static final String TABLE_DONT_EXIST = "The table {} does not exist. Creating it...";
    public static final String TABLE_NOT_CREATE_MESSAGE = "Aun no se a creado la tabla de agendas del mes";
    public static final String SCHEDULE_APPOINTMENT_SUCCESFULL = "Se agenda la cita satisfactoriamente";
    public static final String FAILED_TO_SAVE_APPOINTMENT = "Fallo el proceso de guardado de la cita";
    public static final String FIRST_TIME = "primera vez";
    public static final String DOCTOR_WITHOUT_SCHEDULE = "El doctor no tiene agenda generada";
    public static final String MSG_DOCTOR_WITHOUT_APPOINTMENT_SPACES = "El doctor no cuenta con citas disponibles para el dia";
    public static final String MSG_DOCTOR_WITHOUT_APPOINTMENT_FOR_DAY = "El doctor no cuenta con citas para el dia solicitado";
}
