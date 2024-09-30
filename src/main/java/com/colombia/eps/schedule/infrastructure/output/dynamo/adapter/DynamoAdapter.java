package com.colombia.eps.schedule.infrastructure.output.dynamo.adapter;

import com.colombia.eps.schedule.common.util.Constants;
import com.colombia.eps.schedule.domain.model.AppointmentSpaces;
import com.colombia.eps.schedule.domain.model.CreateSchedule;
import com.colombia.eps.schedule.domain.model.Appointments;
import com.colombia.eps.schedule.domain.model.ScheduleAppointment;
import com.colombia.eps.schedule.domain.spi.ISchedulePersistencePort;
import com.colombia.eps.schedule.infrastructure.exception.*;
import com.colombia.eps.schedule.infrastructure.output.dynamo.config.DynamoDbManager;
import com.colombia.eps.schedule.infrastructure.output.dynamo.entity.ScheduleMonth;
import com.colombia.eps.schedule.infrastructure.output.dynamo.mapper.IDynamoMapper;
import com.colombia.eps.schedule.infrastructure.output.dynamo.repository.IDynamoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class DynamoAdapter implements ISchedulePersistencePort {

    private final IDynamoRepository dynamoRepository;
    private final IDynamoMapper dynamoMapper;

    /**
     * @param createSchedules request for schedule to be created
     * @return message confirmation
     */
    @Override
    public String createSchedule(List<CreateSchedule> createSchedules, YearMonth yearMonth) {

        String tableName = getTableName(yearMonth.atDay(1));
        try (DynamoDbManager manager = new DynamoDbManager()) {
            evaluateStatusTable(manager, tableName);
            List<ScheduleMonth> scheduleMonths = dynamoMapper.toScheduleMonth(createSchedules);
            return dynamoRepository.saveScheduleMonths(scheduleMonths, manager.getEnhancedClient(), manager.createTable(tableName));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new DontCreteSchedulesException(Constants.CREATE_SCHEDULES_MONTH_FAILED);
        }
    }

    private void evaluateStatusTable(DynamoDbManager manager, String tableName) {
        try {
            manager.createTable(tableName).describeTable();
            log.info(Constants.TABLE_EXIST, tableName);
        } catch (ResourceNotFoundException exception) {
            log.info(Constants.TABLE_DONT_EXIST, tableName);
            dynamoRepository.createTableScheduleMonth(manager.getEnhancedClient(), tableName);
        }
    }

    /**
     * @param scheduleAppointment appointment´s request to be save
     * @return message confirmation
     */
    @Override
    public String scheduleAppointment(ScheduleAppointment scheduleAppointment) {
        String tableName = getTableName(scheduleAppointment.getDate());

        try (DynamoDbManager manager = new DynamoDbManager()) {
            DynamoDbTable<ScheduleMonth> table = manager.createTable(tableName);
            ScheduleMonth doctorSchedule = getDoctorSchedule(scheduleAppointment.getDoctorName(), table);

            updateAppointment(doctorSchedule, scheduleAppointment);

            dynamoRepository.updateScheduleMonth(doctorSchedule, table);
            return Constants.SCHEDULE_APPOINTMENT_SUCCESFULL;
        } catch (ResourceNotFoundException e) {
            log.error("Table not created: {}", e.getMessage(), e);
            throw new TableScheduleNotCreatedException(Constants.TABLE_NOT_CREATE_MESSAGE);
        } catch (Exception e) {
            log.error("Failed to save appointment: {}", e.getMessage(), e);
            throw new FailedToSaveAppointmentException(Constants.FAILED_TO_SAVE_APPOINTMENT);
        }

    }

    /**
     * @param appointmentSpaces request os appointment for a day
     * @return list of appointments available for a day required
     */
    @Override
    public List<Appointments> getAppointmentSpaces(AppointmentSpaces appointmentSpaces) {
        String tableName = getTableName(appointmentSpaces.getDate());
        List<Appointments> appointmentsResponse = new ArrayList<>();
        try (DynamoDbManager manager = new DynamoDbManager()) {
            DynamoDbTable<ScheduleMonth> table = manager.createTable(tableName);
            String dateAppointment = appointmentSpaces.getDate().toString();
            if (Constants.FIRST_TIME.equalsIgnoreCase(appointmentSpaces.getDoctorName())) {
                List<ScheduleMonth> scheduleMonths = dynamoRepository.getScheduleMonthsByIndex(Constants.AREA_INDEX, appointmentSpaces.getArea(), table);
                Map<String, Map<String, String>> scheduleOrderByDoctor = generateMapDoctorAppointment(scheduleMonths, dateAppointment);
                appointmentsResponse.addAll(processingForFirstTime(scheduleOrderByDoctor));
            }else{
                ScheduleMonth doctorSchedule = getDoctorSchedule(appointmentSpaces.getDoctorName(), table);
                if (!doctorSchedule.getAppointments().containsKey(dateAppointment)){
                    throw new DoctorWithoutAppointmentForDayExeption(Constants.MSG_DOCTOR_WITHOUT_APPOINTMENT_FOR_DAY);
                }
                Map<String, Map<String, String>> doctorAppointmentSpaces = generateMapDoctorAppointment(List.of(doctorSchedule), dateAppointment);
                if (doctorAppointmentSpaces.get(appointmentSpaces.getDoctorName()).isEmpty() ){
                    throw new DoctorWithoutAppointmentSpacesExeption(Constants.MSG_DOCTOR_WITHOUT_APPOINTMENT_SPACES);
                }
                String doctorName = appointmentSpaces.getDoctorName();
                appointmentsResponse.add(determinateAppointmentsToSet(doctorName, doctorAppointmentSpaces.get(doctorName)));
            }
            return appointmentsResponse;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            if (exception instanceof ResourceNotFoundException) {
                throw new TableScheduleNotCreatedException(Constants.TABLE_NOT_CREATE_MESSAGE);
            }
            throw new FailedToSaveAppointmentException(Constants.FAILED_TO_SAVE_APPOINTMENT);
        }
    }

    private Appointments determinateAppointmentsToSet( String doctorName, Map<String, String> doctorAppointments) {
        Map<String, String> appointmentsToAdd = doctorAppointments.size()<=10 ? doctorAppointments : doctorAppointments.entrySet()
                .stream()
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Appointments appointments = new Appointments();
        appointments.setDoctorName(doctorName);
        appointments.setDoctorAppointments(new HashMap<>(dynamoMapper.toMapWithLocalTime(appointmentsToAdd)));
        return appointments;
    }

    private String getTableName(LocalDate date) {
        return Constants.SCHEDULE + date.getMonth().getDisplayName(TextStyle.FULL, Locale.US).toLowerCase(Locale.ROOT);
    }

    private ScheduleMonth getDoctorSchedule(String doctorName, DynamoDbTable<ScheduleMonth> table) {
        return dynamoRepository.getScheduleByDoctorName(doctorName, table).orElseThrow(()->new DoctorWithoutScheduleExeption(Constants.DOCTOR_WITHOUT_SCHEDULE));
    }

    private void updateAppointment(ScheduleMonth doctorSchedule, ScheduleAppointment scheduleAppointment) {
        Map<String, Map<String, String>> appointments = doctorSchedule.getAppointments();

        String dateKey = scheduleAppointment.getDate().toString();
        String timeKey = scheduleAppointment.getTime().toString();

        Map<String, String> dayAppointments = appointments.computeIfAbsent(dateKey, k -> new HashMap<>());

        dayAppointments.put(timeKey, scheduleAppointment.getPatientName());
    }

    /**
     * @param scheduleOrderByDoctor map with available appointments for each doctor
     * @return list of appointments
     * The scheduleOrderByDoctor map is iterated to obtain an equal number of appointments from each doctor
     * and completing a total of 10 appointments in scheduleResponse which is the list that is returned
     */
    private Collection<? extends Appointments> processingForFirstTime(Map<String, Map<String, String>> scheduleOrderByDoctor) {

        long countDoctorsAvailability = countDoctorsWithAppointmentsAvailabilities(scheduleOrderByDoctor);
        if (countDoctorsAvailability > 0) {
            return processingDoctorsWithAvailableAppointments(scheduleOrderByDoctor, countDoctorsAvailability);
        }
        return new ArrayList<>();
    }

    /**
     * @param scheduleOrderByDoctor    map with available appointments for each doctor
     * @param countDoctorsAvailability number of doctor with available appointments
     * @return list of appointments
     */
    private List<Appointments> processingDoctorsWithAvailableAppointments(Map<String, Map<String, String>> scheduleOrderByDoctor, long countDoctorsAvailability) {
        List<Appointments> appointmentsResponse = new ArrayList<>();
        int appointmentsByDoctor = (int) (10 / countDoctorsAvailability);
        AtomicInteger residue = new AtomicInteger((int) (10 % countDoctorsAvailability));
        int repetitions = 0;
        long countAvailabilities = 1;
        while (appointmentsResponse.size() < 10 && countAvailabilities > 0) {
            Iterator<Map.Entry<String, Map<String, String>>> iterator = scheduleOrderByDoctor.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Map<String, String>> entry = iterator.next();
                String key = entry.getKey();
                Map<String, String> appointmentsAvailable = entry.getValue();

                if (!appointmentsAvailable.isEmpty()) {
                    appointmentsResponse.add(processingAppointmentsAvailable(key, appointmentsAvailable, appointmentsByDoctor, repetitions, residue));
                    if (appointmentsAvailable.isEmpty()) {
                        iterator.remove();
                    }

                } else {
                    iterator.remove();
                }
            }
            repetitions++;
            if (residue.get() == 0 && repetitions > 0 ) {
                break;
            }
            countAvailabilities = countDoctorsWithAppointmentsAvailabilities(scheduleOrderByDoctor);
        }
        return appointmentsResponse;
    }

    /**
     * @param scheduleOrderByDoctor map with available appointments for each doctor
     * @return number of doctors with available appointments
     */
    private long countDoctorsWithAppointmentsAvailabilities(Map<String, Map<String, String>> scheduleOrderByDoctor) {
        return scheduleOrderByDoctor.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .count();
    }

    /**
     * @param scheduleMonths scheduleMonth's list get from the dynamo table
     * @param filterKey      date to which the appointments to be displayed must belong
     * @return map with doctors and appointments available of the day that represents filterKey
     */
    private static Map<String, Map<String, String>> generateMapDoctorAppointment(List<ScheduleMonth> scheduleMonths, String filterKey) {
        return scheduleMonths.stream()
                .filter(scheduleMonth -> scheduleMonth.getAppointments().containsKey(filterKey))
                .collect(Collectors.toMap(
                        ScheduleMonth::getDoctorName,
                        scheduleMonth -> {
                            Map<String, String> appointmentsAvailable = new HashMap<>();
                            scheduleMonth.getAppointments().get(filterKey)
                                    .forEach((key, value) -> {
                                        if (value.equals(Constants.AVAILABLE)) {
                                            appointmentsAvailable.put(key, value);
                                        }
                                    });
                            return appointmentsAvailable;
                        }
                ));
    }

    /**
     * @param doctorName           whose available appointments are processed
     * @param appointments         doctor´s appointments to process
     * @param appointmentsByDoctor number of appointments that is process for each doctor
     * @param finalRepetitions     number of repetitions with processing data
     * @param residue              of the division more appointment space to fill in a doctor
     * @return map with appointment´s list, value of residue and appointment´s list for the doctor after processing
     */
    private Appointments processingAppointmentsAvailable(String doctorName, Map<String, String> appointments, int appointmentsByDoctor, int finalRepetitions, AtomicInteger residue) {

        Appointments appointmentsSpaces = new Appointments();
        appointmentsSpaces.setDoctorName(doctorName);
        Map<String, String> appointmentsToAdd;
        if (appointments.size() <= appointmentsByDoctor) {
            if ((appointments.size() < appointmentsByDoctor) && (finalRepetitions == 0)) {
                residue.set(residue.get() + (appointmentsByDoctor - appointments.size()));
            }
            appointmentsToAdd = appointments;
        } else {
            int limit;
            if (residue.get() == 0) {
                limit = appointmentsByDoctor;
            } else if (finalRepetitions > 0) {
                limit = 1;
            } else {
                limit = appointmentsByDoctor + 1;
            }
            appointmentsToAdd = appointments.entrySet()
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            residue.set(residue.get()-(limit-appointmentsByDoctor));
        }
        appointmentsToAdd.keySet().forEach(appointments::remove);
        appointmentsSpaces.setDoctorAppointments(new HashMap<>(dynamoMapper.toMapWithLocalTime(appointmentsToAdd)));
        return appointmentsSpaces;
    }
}
