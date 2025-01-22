package com.app.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry}")
    private int expiry;

    private Algorithm algorithm;

    @PostConstruct
    public void postConstruct(){
         algorithm = Algorithm.HMAC256(algorithmKey);

    }

    public String generateToken(String username) {
        // Implement JWT generation logic
      return   JWT.create().
                withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis()+expiry))
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
