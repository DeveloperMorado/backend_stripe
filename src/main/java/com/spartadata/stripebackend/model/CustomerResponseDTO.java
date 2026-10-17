package com.spartadata.stripebackend.model;


import com.stripe.model.Address;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerResponseDTO {
    private String id;
    private String email;
    private String name;
    private Address address;
    // Add other fields you want to expose

    public CustomerResponseDTO(com.stripe.model.Customer customer) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.name = customer.getName();
        this.address = customer.getAddress();
    }
}