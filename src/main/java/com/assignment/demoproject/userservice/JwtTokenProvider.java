package com.assignment.demoproject.userservice;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    //additional methods for validating token could be added

    public static SecretKey getSigningKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    SecretKey signingKey = getSigningKey(jwtSecret);
    public String generateToken() {
        User userDetails = new User();

        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(new Date())
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

}
