package com.colombia.eps.schedule.infrastructure.output.feing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatesResponse {
    private String city;
    private List<LocalDate> holidays;
}
