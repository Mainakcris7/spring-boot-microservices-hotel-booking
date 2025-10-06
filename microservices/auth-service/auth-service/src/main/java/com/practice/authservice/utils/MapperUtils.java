package com.practice.authservice.utils;

import com.practice.authservice.dto.CustomerRegistrationDto;
import com.practice.authservice.dto.CustomerResponseDto;
import com.practice.authservice.dto.CustomerUpdateDto;
import com.practice.authservice.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class MapperUtils {

    public static Customer mapCustomerFromCustomerRegistrationDto(CustomerRegistrationDto dto){
        Customer customer = new Customer();
        customer.setId(0);
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(dto.getPassword());
        customer.setAddress(dto.getAddress());
        customer.setGender(dto.getGender());
        customer.setRole(dto.getRole());
        return customer;
    }

    public static CustomerResponseDto mapCustomerResponseDtoFromCustomer(Customer customer){
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setGender(customer.getGender());
        dto.setAddress(customer.getAddress());

        return dto;
    }

    public static Customer mapCustomerFromCustomerUpdateDto(CustomerUpdateDto dto, Customer toBeUpdatedCustomer){
        toBeUpdatedCustomer.setName(dto.getName());
        toBeUpdatedCustomer.setAddress(dto.getAddress());
        toBeUpdatedCustomer.setGender(dto.getGender());
        toBeUpdatedCustomer.setRole(dto.getRole());

        return toBeUpdatedCustomer;
    }
}
