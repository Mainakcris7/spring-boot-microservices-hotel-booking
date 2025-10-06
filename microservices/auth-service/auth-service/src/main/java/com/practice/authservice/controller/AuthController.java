package com.practice.authservice.controller;

import com.practice.authservice.dto.*;
import com.practice.authservice.service.AuthService;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService service;

//    @Observed(name = "register.user", contextualName = "register-user")
    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDto> registerCustomer(@Valid @RequestBody CustomerRegistrationDto dto){
        return service.registerCustomer(dto);
    }

//    @Observed(name = "login.user", contextualName = "login-user")
    @PostMapping("/login")
    public ResponseEntity<JwtDto> loginCustomer(@Valid @RequestBody LoginDto dto){
        return service.loginCustomer(dto);
    }

//    @Observed(name = "validate.jwt", contextualName = "validate-jwt")
    @PostMapping("/validate")
    public ResponseEntity<JwtValidationDto> validateCustomer(@Valid @RequestBody JwtDto dto){
        return service.validateRequest(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@Valid @RequestBody CustomerUpdateDto dto){
        return service.updateCustomer(dto);
    }
}
