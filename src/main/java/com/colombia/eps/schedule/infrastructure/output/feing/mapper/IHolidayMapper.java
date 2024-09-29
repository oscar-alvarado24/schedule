package com.colombia.eps.schedule.infrastructure.output.feing.mapper;

import com.colombia.eps.schedule.infrastructure.output.feing.entity.DatesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IHolidayMapper {
    IHolidayMapper INSTANCE = Mappers.getMapper(IHolidayMapper.class);
    default Map<String, List<LocalDate>> toMap(List<DatesResponse> datesResponse){
        Map<String, List<LocalDate>> map = new HashMap<>();
        datesResponse.forEach(datesResponseProcess -> {
            map.put(datesResponseProcess.getCity(), datesResponseProcess.getHolidays());
        });

        return map;
    }
}
