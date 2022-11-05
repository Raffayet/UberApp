package com.example.uberbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenGenerator {

    @Autowired
    private Environment env;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + 700000);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, env.getProperty("jwt.secret"))
                .compact();

        return token;
    }

    public String getUsernameFromJwtToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(env.getProperty("jwt.secret"))
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(env.getProperty("jwt.secret")).parseClaimsJws(token);
            return true;
        }catch(Exception ex){
            throw new AuthenticationCredentialsNotFoundException("Jwt was expired or incorrect!");
        }
    }
}
