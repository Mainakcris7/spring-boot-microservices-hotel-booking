package com.practice.roomservice.apiclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service")
public interface BookingApiClient {
    @DeleteMapping("/bookings/room/{id}")
    Void deleteBookingsByRoomId(@PathVariable int id);
}
