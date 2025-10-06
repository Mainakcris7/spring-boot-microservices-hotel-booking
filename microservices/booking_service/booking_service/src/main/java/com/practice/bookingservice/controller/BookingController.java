package com.practice.bookingservice.controller;

import com.practice.bookingservice.dto.BookingConfirmationDto;
import com.practice.bookingservice.dto.BookingSaveDto;
import com.practice.bookingservice.dto.BookingUpdateDto;
import com.practice.bookingservice.model.Booking;
import com.practice.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService service;

    @GetMapping("")
    public ResponseEntity<List<Booking>> getAllBookings(){
        return service.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable int id){
        return service.getBookingById(id);
    }

    @PostMapping("")
    public ResponseEntity<BookingConfirmationDto> saveBooking(@RequestBody @Valid BookingSaveDto dto){
        return service.saveBooking(dto);
    }

    @PutMapping("")
    public ResponseEntity<BookingConfirmationDto> updateBooking(@RequestBody @Valid BookingUpdateDto dto){
        return service.updateBooking(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable int id){
        return service.deleteBookingById(id);
    }

    @DeleteMapping("/room/{id}")
    public ResponseEntity<Void> deleteBookingsByRoomId(@PathVariable int id){
        return service.deleteBookingsByRoomId(id);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteBookingsByCustomerId(@PathVariable int id){
        return service.deleteBookingsByCustomerId(id);
    }
}
