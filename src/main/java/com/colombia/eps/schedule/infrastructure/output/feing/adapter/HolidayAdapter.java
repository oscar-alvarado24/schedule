package com.colombia.eps.schedule.infrastructure.output.feing.adapter;

import com.colombia.eps.schedule.domain.spi.IHolidayPersistencePort;
import com.colombia.eps.schedule.infrastructure.output.feing.client.IHolidayClient;
import com.colombia.eps.schedule.infrastructure.output.feing.mapper.IHolidayMapper;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class HolidayAdapter implements IHolidayPersistencePort {
    private final IHolidayClient holidayClient;
    private final IHolidayMapper holidayMapper;

    /**
     * @param cityList
     * @return
     */
    @Override
    public Map<String, List<LocalDate>> getHolidays(Set<String> cityList) {
        return holidayMapper.toMap(holidayClient.getHolidays(cityList.stream().toList()));
    }
}
