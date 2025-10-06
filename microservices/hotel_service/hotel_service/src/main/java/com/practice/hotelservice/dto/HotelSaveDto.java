package com.practice.hotelservice.dto;

import com.practice.hotelservice.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelSaveDto {
    @NotNull(message = "Hotel name is required")
    @NotBlank(message = "Hotel name is required")
    private String hotelName;

    @NotBlank(message = "Hotel description is required")
    private String description;

    @Valid
    @NotNull(message = "Hotel address is required")
    private Address address;

    @Min(value = 1, message = "Rating must be between 1 to 5 (inclusive)")
    @Max(value = 5, message = "Rating must be between 1 to 5 (inclusive)")
    private int rating;

    @Pattern(regexp = "\\d{10}", message = "Phone number is invalid")
    private String contactNumber;
}
