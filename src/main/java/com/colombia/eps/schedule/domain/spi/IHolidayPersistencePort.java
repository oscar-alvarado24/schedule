package com.colombia.eps.schedule.domain.spi;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IHolidayPersistencePort {
    Map<String, List<LocalDate>> getHolidays(Set<String> cityList);
}
