package com.spartadata.stripebackend.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class UserAccountDTO {
    private String userId;
    private String accountId;
    private String token;

//    private String doingBussinessAs;
//    private String webSite;
    private String email;
//    private String phoneNumber;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    @DynamoDbSortKey
    public String getAccountId() {
        return accountId;
    }

}
