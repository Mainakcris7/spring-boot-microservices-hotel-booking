package com.practice.bookingservice.repository;

import com.practice.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByRoomId(int id);
    List<Booking> findByCustomerId(int id);
}
