package com.colombia.eps.schedule.domain.usecase;

import com.colombia.eps.schedule.domain.api.IHolidaysServicePort;
import com.colombia.eps.schedule.domain.spi.IHolidayPersistencePort;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HolidayUseCase implements IHolidaysServicePort {
    private final IHolidayPersistencePort holidayPersistencePort;

    public HolidayUseCase(IHolidayPersistencePort holidayPersistencePort) {
        this.holidayPersistencePort = holidayPersistencePort;
    }

    /**
     * @param cityList
     * @return
     */
    @Override
    public Map<String, List<LocalDate>> getHolidays(Set<String> cityList) {
        return holidayPersistencePort.getHolidays(cityList);
    }
}
