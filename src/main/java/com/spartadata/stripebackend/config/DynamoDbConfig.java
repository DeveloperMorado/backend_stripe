package com.spartadata.stripebackend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

@Configuration
@Getter
public class DynamoDbConfig {
    @Value("${AWS_REGION}")
    String awsRegion;

    @Value("${AWS_CUSTOMER_TABLE_NAME}")
    String customerTableName;

    // Set for local DynamoDB (e.g. http://localhost:8000); leave empty for real AWS
    @Value("${DYNAMODB_ENDPOINT:}")
    String endpoint;

    // Read from Spring config (.env) so they don't have to be OS environment variables.
    // Leave empty in production to use the default AWS credential chain (IAM role, ~/.aws, etc.)
    @Value("${AWS_ACCESS_KEY_ID:}")
    String accessKeyId;

    @Value("${AWS_SECRET_ACCESS_KEY:}")
    String secretAccessKey;

    @Bean
    public DynamoDbEnhancedClient dynamoDbClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .region(Region.of(awsRegion));

        if (!endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        if (!accessKeyId.isBlank() && !secretAccessKey.isBlank()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKeyId, secretAccessKey)));
        } else {
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(builder.build())
                .build();
    }
}
