package com.practice.bookingservice.utils;

import com.mainak.dto.BookingDetails;
import com.mainak.dto.BookingType;
import com.practice.bookingservice.dto.AddressDto;
import com.practice.bookingservice.dto.CustomerDto;
import com.practice.bookingservice.dto.HotelDto;
import com.practice.bookingservice.dto.RoomDto;
import com.practice.bookingservice.model.Booking;
import org.springframework.stereotype.Component;

public class NotificationUtil {
    public static BookingDetails toBookingDetails(
            BookingType bookingType,
            Booking booking,
            CustomerDto customerDto,
            HotelDto hotelDto,
            RoomDto roomDto
            ){
        BookingDetails bookingDetails = new BookingDetails();

        AddressDto address = hotelDto.getAddress();
        String addressStr = String.format(
                "%s, %s, Pin - %s, %s",
                address.getCity(), address.getState(), address.getPostalCode(), address.getCountry()
        );

        bookingDetails.setBookingId(booking.getId());
        bookingDetails.setBookingType(bookingType);
        bookingDetails.setBookedFrom(booking.getBookedFrom());
        bookingDetails.setBookedUntil(booking.getBookedUntil());
        bookingDetails.setCustomerId(customerDto.getId());
        bookingDetails.setCustomerName(customerDto.getName());
        bookingDetails.setCustomerEmail(customerDto.getEmail());
        bookingDetails.setHotelName(hotelDto.getHotelName());
        bookingDetails.setHotelAddress(addressStr);
        bookingDetails.setRoomId(roomDto.getId());
        bookingDetails.setRoomType(roomDto.getRoomType().toString());
        bookingDetails.setTotalPrice(booking.getTotalPrice());

        return bookingDetails;
    }
}
