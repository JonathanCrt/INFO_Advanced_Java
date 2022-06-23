package com.training.crete.springbootTest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String country;
    private int postcode;
    private String city;
}
