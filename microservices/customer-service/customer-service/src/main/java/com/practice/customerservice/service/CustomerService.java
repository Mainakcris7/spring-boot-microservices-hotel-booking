package com.practice.customerservice.service;

import com.practice.customerservice.apiclient.BookingApiClient;
import com.practice.customerservice.exception.CustomerException;
import com.practice.customerservice.model.Customer;
import com.practice.customerservice.repository.CustomerRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository repo;
    private final BookingApiClient bookingApiClient;

    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers = repo.findAll();
        if(customers.isEmpty()){
            log.error("No customers found.");
            throw new CustomerException("No customers found.", HttpStatus.NOT_FOUND);
        }

        log.info("All customer details returned");
        return ResponseEntity.ok(customers);
    }

    public ResponseEntity<Customer> getCustomerById(int id){
        Customer customer = repo.findById(id).orElse(null);
        if(customer == null){
            log.error("No customer found with id: {}", id);
            throw new CustomerException(String.format("No customer found with id: %d", id), HttpStatus.NOT_FOUND);
        }
        log.info("Customer details returned for customer with id: {}", id);
        return ResponseEntity.ok(customer);
    }

    public ResponseEntity<Void> deleteCustomerById(int id){
        Customer customer = repo.findById(id).orElse(null);
        if(customer == null){
            log.error("Failed to delete customer as no customer found with id: {}", id);
            throw new CustomerException(String.format("Failed to delete customer as no customer found with id: %d", id), HttpStatus.BAD_REQUEST);
        }
        try{
            bookingApiClient.deleteBookingsByCustomerId(id);
        }catch (FeignException.FeignClientException e){
            log.warn(e.getMessage());
        }
        repo.delete(customer);
        log.info("Customer deleted with id: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
