package com.practice.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingConfirmationDto {
    private int id;
    private int customerId;
    private HotelDto hotelDetails;
    private RoomDto roomDetails;
    private double totalPrice;
    private LocalDate bookedFrom;
    private LocalDate bookedUntil;
}
