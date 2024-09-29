package com.colombia.eps.schedule.domain.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IHolidaysServicePort {
    Map<String,List<LocalDate>>  getHolidays(Set<String> cityList);
}
