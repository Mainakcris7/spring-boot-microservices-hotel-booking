package com.practice.authservice.service;

import com.practice.authservice.model.Customer;
import com.practice.authservice.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthCustomerDetailsService implements UserDetailsService {
    private final CustomerRepository repo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = repo.findByEmail(email).orElse(null);
        if(customer == null){
            throw new UsernameNotFoundException("Can't find user with email: " + email);
        }
        return new AuthCustomerDetails(customer);
    }
}
