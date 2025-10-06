package com.practice.bookingservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.mainak.dto.BookingDetails;

@Slf4j
@Service
public class NotificationService {
    private final KafkaTemplate<String, BookingDetails> kafkaTemplate;
    @Value("${kafka.topic.name}")
    private String topicName;

    public NotificationService(KafkaTemplate<String, BookingDetails> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    void sendNotification(String key, BookingDetails bookingDetails){
        log.info("Sending notification...");
        kafkaTemplate.send(topicName, key, bookingDetails);
    }
}
