package com.practice.bookingservice.apiclient;

import com.practice.bookingservice.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service")
public interface RoomApiClient {
    @GetMapping("/rooms/{id}")
    RoomDto getRoomById(@PathVariable int id);
}
