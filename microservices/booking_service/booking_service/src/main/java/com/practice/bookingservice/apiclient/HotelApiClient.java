package com.practice.bookingservice.apiclient;

import com.practice.bookingservice.dto.HotelDto;
import com.practice.bookingservice.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service")
public interface HotelApiClient {
    @GetMapping("/hotels/{id}")
    HotelDto getHotelById(@PathVariable int id);
}
