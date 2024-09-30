package com.colombia.eps.schedule.infrastructure.output.dynamo.repository;

import com.colombia.eps.schedule.infrastructure.output.dynamo.entity.ScheduleMonth;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDynamoRepository {
    void createTableScheduleMonth(DynamoDbEnhancedClient enhancedClient, String tableName);
    String saveScheduleMonths(List<ScheduleMonth> scheduleMonths,DynamoDbEnhancedClient enhancedClient, DynamoDbTable<ScheduleMonth> table);
    List<ScheduleMonth> getScheduleMonthsByIndex(String index,String valueSearch, DynamoDbTable<ScheduleMonth> table);
    Optional<ScheduleMonth> getScheduleByDoctorName(String doctorName, DynamoDbTable<ScheduleMonth> table);
    void updateScheduleMonth(ScheduleMonth scheduleMonth, DynamoDbTable<ScheduleMonth> table);
}
