package com.practice.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDto {
    @NotBlank(message = "Invalid JWT")
    private String jwt;
}
