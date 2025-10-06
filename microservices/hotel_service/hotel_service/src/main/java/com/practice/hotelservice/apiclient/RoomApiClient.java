package com.practice.hotelservice.apiclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service")
public interface RoomApiClient {
    @DeleteMapping("/rooms/hotel/{id}")
    Void deleteRoomsByHotelId(@PathVariable int id);
}
