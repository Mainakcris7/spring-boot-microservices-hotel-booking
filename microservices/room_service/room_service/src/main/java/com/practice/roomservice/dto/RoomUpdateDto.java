package com.practice.roomservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.roomservice.enums.Amenity;
import com.practice.roomservice.enums.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateDto {
    @NotNull(message = "Room id is required")
    private int id;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @Min(value = 100, message = "Price per night must be >= 100")
    private double pricePerNight;

    @NotNull(message = "Room check-in time is required")
    @JsonFormat(pattern = "hh:mm a", locale = "en")
    private LocalTime checkInTime;

    @NotNull(message = "Room check-out time is required")
    @JsonFormat(pattern = "hh:mm a", locale = "en")
    private LocalTime checkOutTime;

    @NotEmpty(message = "Please provide at least on amenity (AC, FRIDGE, WASHING_MACHINE, WIFI)")
    private Set<Amenity> amenities;
}
