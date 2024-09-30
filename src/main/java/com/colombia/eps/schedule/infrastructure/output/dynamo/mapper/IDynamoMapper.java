package com.colombia.eps.schedule.infrastructure.output.dynamo.mapper;

import com.colombia.eps.schedule.domain.model.CreateSchedule;
import com.colombia.eps.schedule.infrastructure.output.dynamo.entity.ScheduleMonth;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDynamoMapper {
    IDynamoMapper INSTANCE = Mappers.getMapper(IDynamoMapper.class);
    List<ScheduleMonth> toScheduleMonth(List<CreateSchedule> createSchedule);
    default Map<LocalTime, String> toMapWithLocalTime(Map<String, String> stringMap){
        Map<LocalTime, String> map = new HashMap<>();
        stringMap.forEach((key, value) -> map.put(LocalTime.parse(key), value));
        return map;
    }
    default Map<LocalDate, String> toMapWithLocalDate(Map<String, String> stringMap){
        Map<LocalDate, String> map = new HashMap<>();
        stringMap.forEach((key, value) -> map.put(LocalDate.parse(key), value));
        return map;
    }
    default Map<LocalDate,Map<LocalTime,String>> toMapWithMap(Map<String,Map<String,String>> stringMap){
        Map<LocalDate,Map<LocalTime,String>> map = new HashMap<>();
        stringMap.forEach((key, value) ->
            map.put(LocalDate.parse(key), toMapWithLocalTime(value))
        );
        return map;
    }

    default Map<String,String> toMapWithStringMap(Map<LocalTime, String> localTimeMap){
        Map<String,String> map = new HashMap<>();
        localTimeMap.forEach((key, value) ->
            map.put(key.toString(),value)
        );
        return map;
    }
    default Map<String,Map<String,String>> toMapWithFieldsString(Map<LocalDate,Map<LocalTime,String>> mapWithFieldsClass){
        Map<String,Map<String,String>> map = new HashMap<>();
        mapWithFieldsClass.forEach((key, value) ->
            map.put(key.toString(),toMapWithStringMap(value))
        );
        return map;
    }
}

