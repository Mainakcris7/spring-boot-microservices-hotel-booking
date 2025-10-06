package com.practice.hotelservice.utils;

import com.practice.hotelservice.dto.HotelSaveDto;
import com.practice.hotelservice.dto.HotelUpdateDto;
import com.practice.hotelservice.model.Hotel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HotelMapperUtil {
    public Hotel mapSaveDtoToHotel(HotelSaveDto dto){
        final Hotel hotel = new Hotel();
        hotel.setId(0);
        hotel.setHotelName(dto.getHotelName());
        hotel.setDescription(dto.getDescription());
        hotel.setAddress(dto.getAddress());
        hotel.setRating(dto.getRating());
        hotel.setContactNumber(dto.getContactNumber());
        hotel.setCreatedAt(LocalDateTime.now());

        return hotel;
    }

    public Hotel mapUpdateDtoToHotel(HotelUpdateDto dto){
        final Hotel hotel = new Hotel();
        hotel.setId(dto.getId());
        hotel.setHotelName(dto.getHotelName());
        hotel.setDescription(dto.getDescription());
        hotel.setAddress(dto.getAddress());
        hotel.setContactNumber(dto.getContactNumber());
        hotel.setRating(dto.getRating());

        return hotel;
    }
}
