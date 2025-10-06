package com.practice.customerservice.apiclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service")
public interface BookingApiClient {
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteBookingsByCustomerId(@PathVariable int id);
}
