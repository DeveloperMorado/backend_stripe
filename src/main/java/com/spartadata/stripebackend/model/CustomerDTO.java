package com.spartadata.stripebackend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

//@Getter
//@Setter
@Data
@DynamoDbBean
public class CustomerDTO {

    String userId;
    String customerId;
    String userEmail;
    String customerName;


    @DynamoDbPartitionKey
    public String getUserId() { return userId; }
//    public void setUserId(String userId) { this.userId = userId; }

    @DynamoDbSortKey
    public String getCustomerId() { return customerId; }
//    public void setCustomerId(String customerId) { this.customerId = customerId; }

//    public String getCustomerName() { return customerName; }
//    public void setCustomerName(String customerName) { this.customerName = customerName; }
//
//    public String getUserEmail() { return userEmail; }
//    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

}
