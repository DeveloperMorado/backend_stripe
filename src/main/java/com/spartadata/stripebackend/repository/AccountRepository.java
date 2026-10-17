package com.spartadata.stripebackend.repository;

import com.spartadata.stripebackend.model.UserAccountDTO;
import com.stripe.model.Account;

public interface AccountRepository {
    UserAccountDTO getUserAccount(String userId);
    void saveAccount(Account account, String userId, String token);
    void updateAccount(Account account, String userId);
    void deleteAccount(UserAccountDTO account, String userId);
}
