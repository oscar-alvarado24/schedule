package com.colombia.eps.schedule.infrastructure.configuration;

import com.colombia.eps.schedule.domain.api.IHolidaysServicePort;
import com.colombia.eps.schedule.domain.api.IScheduleServicePort;
import com.colombia.eps.schedule.domain.spi.IHolidayPersistencePort;
import com.colombia.eps.schedule.domain.spi.ISchedulePersistencePort;
import com.colombia.eps.schedule.domain.usecase.HolidayUseCase;
import com.colombia.eps.schedule.domain.usecase.ScheduleUseCase;
import com.colombia.eps.schedule.infrastructure.output.dynamo.adapter.DynamoAdapter;
import com.colombia.eps.schedule.infrastructure.output.dynamo.mapper.IDynamoMapper;
import com.colombia.eps.schedule.infrastructure.output.dynamo.repository.DynamoRepository;
import com.colombia.eps.schedule.infrastructure.output.dynamo.repository.IDynamoRepository;
import com.colombia.eps.schedule.infrastructure.output.feing.adapter.HolidayAdapter;
import com.colombia.eps.schedule.infrastructure.output.feing.client.IHolidayClient;
import com.colombia.eps.schedule.infrastructure.output.feing.mapper.IHolidayMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IDynamoMapper dynamoMapper;
    private final IHolidayClient holidayClient;
    private final IHolidayMapper holidayMapper;

    @Bean
    public IDynamoRepository dynamoRepository() {
        return new DynamoRepository();
    }

    @Bean
    public ISchedulePersistencePort schedulePersistencePort() {
        return new DynamoAdapter(dynamoRepository(), dynamoMapper);
    }

    @Bean
    public IScheduleServicePort scheduleServicePort() {
        return new ScheduleUseCase(schedulePersistencePort());
    }

    @Bean
    public IHolidayPersistencePort holidayPersistencePort() {
        return new HolidayAdapter(holidayClient, holidayMapper);
    }
    @Bean
    public IHolidaysServicePort holidaysServicePort() {
        return new HolidayUseCase(holidayPersistencePort());
    }
}
