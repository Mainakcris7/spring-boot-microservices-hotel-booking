package com.practice.authservice.dto;

import com.practice.authservice.enums.Gender;
import com.practice.authservice.enums.UserRole;
import com.practice.authservice.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationDto {
    @NotBlank(message = "Please provide name")
    private String name;

    @Email(message = "Please provide valid email id")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,20}$",
            message = "Password must be 8-20 characters long and include at least one letter, one number, and one special character")
    private String password;

    @NotNull(message = "Please provide address")
    private Address address;

    @NotNull(message = "Please provide gender")
    private Gender gender;

    @NotNull(message = "Please provide role")
    private UserRole role;
}
