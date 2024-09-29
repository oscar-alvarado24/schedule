package com.colombia.eps.schedule.infrastructure.output.dynamo.entity;

import com.colombia.eps.schedule.common.util.Constants;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@DynamoDbBean
public class ScheduleMonth {
    private String doctorName;
    private String area;
    @Getter
    private Map<String, Map<String, String>> appointments;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("doctorName")
    public String getDoctorName() {
        return doctorName;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {Constants.AREA_INDEX})
    @DynamoDbAttribute("area")
    public String getArea() {
        return area;
    }
}
