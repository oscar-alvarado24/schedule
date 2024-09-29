package com.colombia.eps.schedule.infrastructure.output.dynamo.repository;

import com.colombia.eps.schedule.common.util.Constants;
import com.colombia.eps.schedule.infrastructure.exception.DontCreatecheduleMonthTableException;
import com.colombia.eps.schedule.infrastructure.exception.DontSavescheduleMonthListException;
import com.colombia.eps.schedule.infrastructure.output.dynamo.entity.ScheduleMonth;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DynamoRepository  implements IDynamoRepository{


    /**
     * @param enhancedClient client of DynamoDbEnhancedClient for work in dynamoDB
     * @param tableName table's name to crete
     */
    @Override
    public void createTableScheduleMonth(DynamoDbEnhancedClient enhancedClient, String tableName) {
        try {
            EnhancedGlobalSecondaryIndex areaGsi = createIndex(Constants.AREA_INDEX);

            CreateTableEnhancedRequest createTableRequest = CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(areaGsi)
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();

            enhancedClient.table(tableName, TableSchema.fromBean(ScheduleMonth.class)).createTable(createTableRequest);
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new DontCreatecheduleMonthTableException(exception.getMessage());
        }
    }

    /**
     * @param scheduleMonths list os schedules for save
     * @param enhancedClient client of DynamoDbEnhancedClient for work in dynamoDB
     * @param table object DynamoDbTable for work with the dynamo table
     * @return message of confirmation
     */
    @Override
    public String saveScheduleMonths(List<ScheduleMonth> scheduleMonths, DynamoDbEnhancedClient enhancedClient, DynamoDbTable<ScheduleMonth> table) {
        try {
            WriteBatch.Builder<ScheduleMonth> writeBatchBuilder = WriteBatch.builder(ScheduleMonth.class)
                    .mappedTableResource(table);

            for (ScheduleMonth scheduleMonth : scheduleMonths) {
                writeBatchBuilder.addPutItem(scheduleMonth);
            }

            BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                    .writeBatches(writeBatchBuilder.build())
                    .build();
            enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
            return Constants.SAVE_SCHEDULE_MONTH_SUCCESSFULLY;
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new DontSavescheduleMonthListException(Constants.SAVE_SCHEDULE_MONTH_FAILED);
        }
    }

    /**
     * @param index of the table to be queried
     * @param valueSearch value to be searched for in the index
     * @param table object DynamoDbTable for work with the dynamo table
     * @return list of scheduleMont
     */
    @Override
    public List<ScheduleMonth> getScheduleMonthsByIndex(String index, String valueSearch, DynamoDbTable<ScheduleMonth> table) {
        DynamoDbIndex<ScheduleMonth> dbIndex = table.index(index);

        // Perform the query for the attribute
        QueryConditional queryConditionalA = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(valueSearch)
                .build());

        SdkIterable<Page<ScheduleMonth>> queryResult = dbIndex.query(r -> r.queryConditional(queryConditionalA));
        return queryResult.stream()
                .parallel()
                .flatMap(page -> page.items().stream())
                .collect(Collectors.toList());
    }

    /**
     * @param doctorName
     * @param table
     * @return
     */
    @Override
    public ScheduleMonth getScheduleByDoctorName(String doctorName, DynamoDbTable<ScheduleMonth> table) {
        Key key = Key.builder().partitionValue(doctorName).build();
        return table.getItem(key);
    }

    /**
     * @param scheduleMonth
     * @param table
     */
    @Override
    public void updateScheduleMonth(ScheduleMonth scheduleMonth, DynamoDbTable<ScheduleMonth> table) {
        table.updateItem(scheduleMonth);
    }


    private EnhancedGlobalSecondaryIndex createIndex(String indexName){
        return EnhancedGlobalSecondaryIndex.builder()
                .indexName(indexName)
                .projection(p -> p.projectionType(ProjectionType.ALL))
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(5L)
                        .writeCapacityUnits(5L)
                        .build())
                .build();
    }
}
