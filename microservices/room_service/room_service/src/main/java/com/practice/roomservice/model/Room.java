package com.practice.roomservice.model;

import com.practice.roomservice.enums.Amenity;
import com.practice.roomservice.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int hotelId;
    @Enumerated(value = EnumType.STRING)
    private RoomType roomType;
    private double pricePerNight;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    @ElementCollection
    @Enumerated(value = EnumType.STRING)
    private Set<Amenity> amenities;
}
