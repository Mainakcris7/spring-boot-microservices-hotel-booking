package com.practice.roomservice.apiclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service")
public interface HotelApiClient {
    @GetMapping("/hotels/exists/{id}")
    boolean hotelExistsById(@PathVariable int id);
}
