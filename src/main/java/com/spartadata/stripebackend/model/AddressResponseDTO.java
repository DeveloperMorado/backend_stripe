package com.spartadata.stripebackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AddressResponseDTO {
    String city;
    String country;
    String line1;
    String line2;
    String postalCode;
    String state;
}
