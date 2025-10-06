package com.mainak.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDetails {
    private int bookingId;
    private BookingType bookingType;
    private int customerId;
    private String customerName;
    private String customerEmail;
    private String hotelName;
    private String hotelAddress;
    private int roomId;
    private String roomType;
    private double totalPrice;
    private LocalDate bookedFrom;
    private LocalDate bookedUntil;
}
