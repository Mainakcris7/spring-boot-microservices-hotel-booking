package com.practice.hotelservice.controller;

import com.practice.hotelservice.dto.HotelSaveDto;
import com.practice.hotelservice.dto.HotelUpdateDto;
import com.practice.hotelservice.model.Hotel;
import com.practice.hotelservice.service.HotelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@AllArgsConstructor
public class HotelController {
    private final HotelService service;

    @GetMapping("")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        return service.getAllHotels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable int id) {
        return service.getHotelById(id);
    }

    @GetMapping("/exists/{id}")
    public Boolean hotelExistsById(@PathVariable int id) {
        return service.hotelExistsById(id);
    }

    @PostMapping("")
    public ResponseEntity<Hotel> saveHotel(@RequestBody @Valid HotelSaveDto dto) {
        return service.saveHotel(dto);
    }

    @PutMapping("")
    public ResponseEntity<Hotel> updateHotel(@RequestBody @Valid HotelUpdateDto dto) {
        return service.updateHotel(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateHotel(@PathVariable int id) {
        return service.deleteHotelById(id);
    }
}
