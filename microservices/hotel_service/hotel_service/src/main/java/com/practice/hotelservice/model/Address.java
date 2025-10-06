package com.practice.hotelservice.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @NotBlank(message = "Please provide country in address details")
    private String country;

    @NotBlank(message = "Please provide state in address details")
    private String state;

    @NotBlank(message = "Please provide city in address details")
    private String city;

    @Pattern(regexp = "\\d{6}", message = "Invalid postal code")
    private String postalCode;
}
