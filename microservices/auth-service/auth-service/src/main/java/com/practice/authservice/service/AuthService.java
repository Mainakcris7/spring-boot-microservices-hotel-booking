package com.practice.authservice.service;

import com.practice.authservice.dto.*;
import com.practice.authservice.exception.AuthException;
import com.practice.authservice.model.Customer;
import com.practice.authservice.repository.CustomerRepository;
import com.practice.authservice.utils.JwtUtils;
import com.practice.authservice.utils.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private final CustomerRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public ResponseEntity<CustomerResponseDto> registerCustomer(CustomerRegistrationDto dto){
        Customer customer = MapperUtils.mapCustomerFromCustomerRegistrationDto(dto);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        Customer savedCustomer = repo.save(customer);

        CustomerResponseDto responseDto = MapperUtils.mapCustomerResponseDtoFromCustomer(savedCustomer);

        log.info("Customer registered with id: {}", savedCustomer.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    public ResponseEntity<JwtDto> loginCustomer(LoginDto dto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        if(auth.isAuthenticated()){
            JwtDto jwtDto = new JwtDto(jwtUtils.generateJwtToken(dto.getEmail()));

            log.info("Customer logged in with email: {}", dto.getEmail());
            return ResponseEntity.ok(jwtDto);
        }

        log.error("Login failed: Please provide valid credentials");
        throw new AuthException("Login failed: Please provide valid credentials", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<JwtValidationDto> validateRequest(JwtDto dto){
        JwtValidationDto validationDto = jwtUtils.validateToken(dto.getJwt());
        if(validationDto.isValid()){
            log.info("Jwt validation successful");
            return ResponseEntity.ok(validationDto);
        }
        log.error("Invalid JWT provided.");
        throw new AuthException("Invalid JWT provided.", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<CustomerResponseDto> updateCustomer(CustomerUpdateDto dto){
        Customer toBeUpdatedCustomer = repo.findById(dto.getId()).orElse(null);
        if(toBeUpdatedCustomer == null){
            log.error("No customer found with id: {}", dto.getId());
            throw new AuthException(String.format("No customer found with id: %d", dto.getId()), HttpStatus.BAD_REQUEST);
        }
        Customer customer = MapperUtils.mapCustomerFromCustomerUpdateDto(dto, toBeUpdatedCustomer);

        customer.setPassword(passwordEncoder.encode(dto.getPassword()));

        Customer updatedCustomer = repo.save(customer);

        log.info("Customer details updated for customer with id: {}", updatedCustomer.getId());
        return ResponseEntity.ok(MapperUtils.mapCustomerResponseDtoFromCustomer(updatedCustomer));
    }
}