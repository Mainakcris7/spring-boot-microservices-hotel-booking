package com.practice.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private int id;
    private String hotelName;
    private AddressDto address;
    private String contactNumber;
}
