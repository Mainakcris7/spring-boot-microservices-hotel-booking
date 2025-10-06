package com.practice.bookingservice.dto;

import com.practice.bookingservice.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private int id;
    private int hotelId;
    private double pricePerNight;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private RoomType roomType;
}
