package com.peoplesuite.employee.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final String tableName = "Client_Credentials";
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

    public boolean isTokenValid(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":tokenVal", AttributeValue.fromS(accessToken));

            QueryRequest queryRequest = QueryRequest.builder()
                    .tableName(tableName)
                    .indexName("access_token-index")
                    .keyConditionExpression("access_token = :tokenVal")
                    .expressionAttributeValues(expressionValues)
                    .build();

            QueryResponse response = dynamoDbClient.query(queryRequest);

            return response.count() > 0;
        }
        return false;
    }
}
