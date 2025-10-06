package com.practice.notificationservice.utils;

import com.mainak.dto.BookingDetails;

public class EmailNotificationUtil {
    public static String getBookingCreatedEmailBody(BookingDetails bookingDetails){
       return String.format("""
                Dear %s,
            
                Thank you for booking with us!
                
                Here are your booking details:
                - Booking ID: %d
                - Hotel Name: %s
                - Hotel Address: %s
                - Room ID: %d
                - Room Type: %s
                - Check-in Date: %s
                - Check-out Date: %s
                - Total Price: ₹%.2f
            
                We look forward to hosting you and ensuring a comfortable stay.
            
                Warm regards,
                Hotel Reservations Team
                """,
                bookingDetails.getCustomerName(),
                bookingDetails.getBookingId(),
                bookingDetails.getHotelName(),
                bookingDetails.getHotelAddress(),
                bookingDetails.getRoomId(),
                bookingDetails.getRoomType(),
                bookingDetails.getBookedFrom(),
                bookingDetails.getBookedUntil(), bookingDetails.getTotalPrice()
        );
    }
    public static String getBookingCancelEmailBody(BookingDetails bookingDetails){
        return String.format("""
                Dear %s,
            
                Your booking at %s has been cancelled.
            
                Booking ID: %d
                Room ID: %d
                Room Type: %s
                Check-in: %s
                Check-out: %s
            
                The total amount of ₹%.2f will be reimbursed soon!
                
                If this was unintentional, please contact our support team.
            
                Regards,
                Hotel Reservations Team
                """,
                bookingDetails.getCustomerName(),
                bookingDetails.getHotelName(),
                bookingDetails.getBookingId(),
                bookingDetails.getRoomId(),
                bookingDetails.getRoomType(),
                bookingDetails.getBookedFrom(),
                bookingDetails.getBookedUntil(),
                bookingDetails.getTotalPrice()

        );

    }
}
