package com.practice.bookingservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaveDto {
    @NotNull(message = "Hotel id is required")
    private int hotelId;

    @NotNull(message = "Room id is required")
    private int roomId;

    @NotNull(message = "Customer id is required")
    private int customerId;

    @NotNull(message = "Please provide booking start date")
    @Future(message = "Booking start date must be in future")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate bookedFrom;

    @NotNull(message = "Please provide booking end date")
    @Future(message = "Booking end date must be in future")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate bookedUntil;
}
