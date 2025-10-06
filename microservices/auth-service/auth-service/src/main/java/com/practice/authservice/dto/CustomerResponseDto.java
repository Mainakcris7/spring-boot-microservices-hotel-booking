package com.practice.authservice.dto;

import com.practice.authservice.enums.Gender;
import com.practice.authservice.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
    private int id;
    private String name;
    private String email;
    private Address address;
    private Gender gender;
}
