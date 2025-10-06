package com.practice.notificationservice.service;

import com.mainak.dto.BookingDetails;

public interface NotificationService {
    void sendNotification(BookingDetails bookingDetails);
}
