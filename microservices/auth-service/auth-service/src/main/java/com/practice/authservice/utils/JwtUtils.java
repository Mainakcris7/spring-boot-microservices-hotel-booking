package com.practice.authservice.utils;

import com.practice.authservice.dto.JwtValidationDto;
import com.practice.authservice.model.Customer;
import com.practice.authservice.repository.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtils {
    private String SECRET_KEY;
    @Autowired
    private CustomerRepository repo;

    public JwtUtils() {
        try {
            // Generate a new key and encode it
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            SECRET_KEY = Base64.getEncoder().encodeToString(sk.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
    }

    public SecretKey getSecureKey() {
        // Generate HMAC-SHA version of the key
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateJwtToken(String userEmail) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userEmail)
                .issuedAt(new Date(System.currentTimeMillis()))
                // 7 days validity after the creation of the token
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .and()
                .signWith(getSecureKey())
                .compact();
    }

    // Extracting user email from token
    public String extractUserEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private JwtValidationDto getValidationResponse(String email){
        Customer customer = repo.findByEmail(email).orElse(null);

        if(customer == null)
            return null;

        return new JwtValidationDto(
                customer.getEmail(),
                customer.getRole().name(),
                true
        );
    }

    // Validate token
    public JwtValidationDto validateToken(String jwtToken) {
        JwtValidationDto notValidated = new JwtValidationDto(null, null, false);
        if(!isTokenExpired(jwtToken)){
            try {
                String email = extractUserEmail(jwtToken);
                return getValidationResponse(email);
            } catch (Exception e) {
                return notValidated;
            }
        }
        return notValidated;
    }

    // Check if the token has expired
    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    // Extracting expiration date from token
    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    // Extracting claims from token
    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    // Extracting all claims from token
    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getSecureKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
}
