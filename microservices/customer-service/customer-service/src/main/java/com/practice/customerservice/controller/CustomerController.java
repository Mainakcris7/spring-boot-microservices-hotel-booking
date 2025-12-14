package com.practice.customerservice.controller;

import com.practice.customerservice.dto.CustomerDto;
import com.practice.customerservice.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;

    @GetMapping("")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable int id) {
        return service.getCustomerById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable int id) {
        return service.deleteCustomerById(id);
    }
}
