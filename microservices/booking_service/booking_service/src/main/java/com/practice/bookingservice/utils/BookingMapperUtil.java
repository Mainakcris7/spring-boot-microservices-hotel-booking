package com.practice.bookingservice.utils;

import com.practice.bookingservice.dto.*;
import com.practice.bookingservice.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapperUtil {
    public Booking mapFromSaveDto(BookingSaveDto dto){
        Booking booking = new Booking();
        booking.setId(0);
        booking.setHotelId(dto.getHotelId());
        booking.setRoomId(dto.getRoomId());
        booking.setTotalPrice(0);
        booking.setCustomerId(dto.getCustomerId());
        booking.setBookedFrom(dto.getBookedFrom());
        booking.setBookedUntil(dto.getBookedUntil());

        return booking;
    }

    public Booking mapFromUpdateDto(BookingUpdateDto dto, Booking booking){
        booking.setBookedFrom(dto.getBookedFrom());
        booking.setBookedUntil(dto.getBookedUntil());

        return booking;
    }

    public BookingConfirmationDto mapConfirmationDtoFromBooking(Booking booking, HotelDto hotelDto, RoomDto roomDto){
        BookingConfirmationDto dto = new BookingConfirmationDto();
        dto.setId(booking.getId());
        dto.setHotelDetails(hotelDto);
        dto.setRoomDetails(roomDto);
        dto.setCustomerId(booking.getCustomerId());
        dto.setBookedFrom(booking.getBookedFrom());
        dto.setBookedUntil(booking.getBookedUntil());
        dto.setTotalPrice(booking.getTotalPrice());

        return dto;
    }
}
