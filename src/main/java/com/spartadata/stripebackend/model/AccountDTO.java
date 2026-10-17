package com.spartadata.stripebackend.model;

import com.stripe.model.Address;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountDTO {
    private String name;
    private String email;

    private Address address;

}

