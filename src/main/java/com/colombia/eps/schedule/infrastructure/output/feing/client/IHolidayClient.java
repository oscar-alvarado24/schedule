package com.colombia.eps.schedule.infrastructure.output.feing.client;

import com.colombia.eps.schedule.infrastructure.output.feing.entity.DatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "HolidaysClient", url = "${holidays.service.url}")
public interface IHolidayClient {
    @GetMapping("/get-holidays")
    List<DatesResponse> getHolidays(@RequestParam(name = "city") List<String> city);
}
