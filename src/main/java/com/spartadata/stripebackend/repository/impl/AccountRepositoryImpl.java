package com.spartadata.stripebackend.repository.impl;

import com.spartadata.stripebackend.config.DynamoDbConfig;
import com.spartadata.stripebackend.model.UserAccountDTO;
import com.spartadata.stripebackend.repository.AccountRepository;
import com.stripe.model.Account;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbConfig dynamoDBConfig;

    public AccountRepositoryImpl(
            DynamoDbEnhancedClient dynamoDbEnhancedClient,
            DynamoDbConfig dynamoDbConfig) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.dynamoDBConfig = dynamoDbConfig;
    }

    @Override
    public UserAccountDTO getUserAccount(String userId) throws DynamoDbException {
        try {
            DynamoDbTable<UserAccountDTO> customer = dynamoDbEnhancedClient.table(
                    dynamoDBConfig.getCustomerTableName(),
                    TableSchema.fromBean(UserAccountDTO.class));

            PageIterable<UserAccountDTO> results = customer.query(
                r -> r.queryConditional(
                        QueryConditional.keyEqualTo(
                                Key.builder().partitionValue(userId).build()
                        )
                )
            );

            return results.items().stream()
                    .findFirst()
                    .orElse(null);
        } catch(DynamoDbException e) {
            System.out.println("Querying with userId: " + userId);
            System.out.println("Error: " + e);
            int a = 10;
        }
        return null;
    }

    @Override
    public void saveAccount(Account account, String userId, String token){
        try {
            DynamoDbTable<UserAccountDTO> customer = dynamoDbEnhancedClient.table(
                    dynamoDBConfig.getCustomerTableName(),
                    TableSchema.fromBean(UserAccountDTO.class));

            UserAccountDTO userAccount = new UserAccountDTO();
            userAccount.setAccountId(account.getId());
            userAccount.setUserId(userId);
            userAccount.setEmail(account.getEmail());

            //This is used for validate the return and refresh
            userAccount.setToken(token);

            customer.putItem(userAccount);
        } catch(DynamoDbException e) {
            System.out.println("Error saving account.UserId: " + userId);
            System.out.println("Error: " + e);
            //TODO I have to take action no just print the error
        }
    }

    @Override
    public void updateAccount(Account account, String userId){
        try {
            DynamoDbTable<UserAccountDTO> userAccount = dynamoDbEnhancedClient.table(
                    dynamoDBConfig.getCustomerTableName(),
                    TableSchema.fromBean(UserAccountDTO.class));

            UserAccountDTO existingAccount = getUserAccount(userId);

            existingAccount.setEmail(account.getEmail());
            userAccount.updateItem(existingAccount);
        } catch(DynamoDbException e) {
            System.out.println("Error updating account.UserId: " + userId);
            System.out.println("Error: " + e);
            //TODO I have to take action no just print the error
        }
    }

    @Override
    public void deleteAccount(UserAccountDTO account, String userId){
        try {
            DynamoDbTable<UserAccountDTO> userAccount = dynamoDbEnhancedClient.table(
                    dynamoDBConfig.getCustomerTableName(),
                    TableSchema.fromBean(UserAccountDTO.class));

            UserAccountDTO deleteItem = new UserAccountDTO();
            deleteItem.setUserId(userId);
            deleteItem.setAccountId(account.getAccountId());

            BatchWriteItemEnhancedRequest request = BatchWriteItemEnhancedRequest.builder()
                    .writeBatches(WriteBatch.builder(UserAccountDTO.class)
                                    .mappedTableResource(userAccount)
                                    .addDeleteItem(deleteItem)
                                    .build())
                    .build();

            dynamoDbEnhancedClient.batchWriteItem(request);
        } catch(DynamoDbException e) {
            System.out.println("Delete userAccount.UserId: " + userId);
            System.out.println("Error: " + e);
            //TODO I have to take action no just print the error
        }
    }
}
