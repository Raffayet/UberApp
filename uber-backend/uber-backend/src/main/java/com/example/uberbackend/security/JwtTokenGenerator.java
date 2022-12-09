package com.example.uberbackend.security;

import com.example.uberbackend.dto.UserDto;
import com.example.uberbackend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenGenerator {

    @Autowired
    private Environment env;

    public String generateToken(User u){
        String username = u.getEmail();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + 700000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", u.getRole().getAuthority());
        claims.put("userDto", new UserDto(u));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, env.getProperty("jwt.secret"))
                .compact();
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
            throw new AuthenticationCredentialsNotFoundException("You were not authenticated, please log in!");
        }
    }
}
