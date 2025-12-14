package com.practice.bookingservice.apiclient;

import com.practice.bookingservice.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerApiClient {
    @GetMapping("/customers/{id}")
    CustomerDto getCustomerById(@PathVariable int id);
}
