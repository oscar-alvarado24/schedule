package com.colombia.eps.schedule.infrastructure.output.dynamo.config;


import com.colombia.eps.schedule.common.util.Constants;
import com.colombia.eps.schedule.infrastructure.exception.DynamoDbManagerException;
import com.colombia.eps.schedule.infrastructure.output.dynamo.entity.ScheduleMonth;
import lombok.Getter;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static com.colombia.eps.library.GenerateSession.generateSession;

public class DynamoDbManager implements AutoCloseable {
    private final DynamoDbClient dynamoDbClient;
    @Getter
    private final DynamoDbEnhancedClient enhancedClient;


    public DynamoDbManager() {
        try {
            String dynamoRole = System.getenv(Constants.DYNAMO_ROL);
            StaticCredentialsProvider credential = generateSession(dynamoRole, Constants.ROLE_SESSION_NAME_DYNAMO);
            this.dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(credential)
                    .build();
            this.enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();

        }
        catch (Exception exception){
            throw new DynamoDbManagerException(exception.getMessage());
        }
    }

    @Override
    public void close() {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    }

    public DynamoDbTable<ScheduleMonth> createTable(String tableName){
        return this.enhancedClient.table(tableName, TableSchema.fromBean(ScheduleMonth.class));
    }
}
