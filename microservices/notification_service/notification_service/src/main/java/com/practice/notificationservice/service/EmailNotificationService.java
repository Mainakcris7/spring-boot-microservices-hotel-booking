package com.practice.notificationservice.service;

import com.mainak.dto.BookingDetails;
import com.mainak.dto.BookingType;
import com.practice.notificationservice.utils.EmailNotificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService{

    @KafkaListener(topics = "${kafka.topic.name:booking}", groupId = "consumer-grp-1")
    @Override
    public void sendNotification(BookingDetails bookingDetails) {

        String mailBody = switch (bookingDetails.getBookingType()) {
            case CREATE -> EmailNotificationUtil.getBookingCreatedEmailBody(bookingDetails);
            case CANCEL -> EmailNotificationUtil.getBookingCancelEmailBody(bookingDetails);
        };

        log.info("Sending email to {} ...", bookingDetails.getCustomerEmail());
        log.info(mailBody);

    }
}
