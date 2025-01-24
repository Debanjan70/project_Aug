package com.app.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
    private long expiry;

    private Algorithm algorithm;

//    private String USER_NAME = "username";

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

    public String getUserName(String token){
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
        return decodedJWT.getClaim("username").asString();
    }
}
