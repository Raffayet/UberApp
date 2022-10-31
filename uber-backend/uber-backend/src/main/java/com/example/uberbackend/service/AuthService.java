package com.example.uberbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.uberbackend.dto.UserDataDto;
import com.example.uberbackend.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${jwt.secret}")
    private String secret;
    private final static int FIVE_MINUTES = 5 * 60 * 1000;
    private final static int ONE_WEEK = 7 * 24 * 60 * 1000;


    private final  UserService userService;

    public Collection<SimpleGrantedAuthority> getAuthorityClaimsFromJWT(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm=Algorithm.HMAC256(secret.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }

    public String getUsernameFromJWT(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm=Algorithm.HMAC256(secret.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getSubject();
    }

    public String makeAccessToken(UserDetails user, HttpServletRequest request) {
        Algorithm algorithm=Algorithm.HMAC256(secret.getBytes());
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + FIVE_MINUTES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String makeAccessToken(User user, HttpServletRequest request) {
        Algorithm algorithm=Algorithm.HMAC256(secret.getBytes());
        return JWT.create().withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + FIVE_MINUTES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", user.getRole().getRole())
                .sign(algorithm);
    }

    public String makeRefreshToken(String username, HttpServletRequest request) {
        Algorithm algorithm=Algorithm.HMAC256(secret.getBytes());
        return JWT.create().withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ONE_WEEK))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                String username = getUsernameFromJWT(authorizationHeader);
                User user = userService.getUser(username).get();
                String accessToken = makeAccessToken(user,request);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                requestingNewAccessTokenError(response, exception);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    private static void requestingNewAccessTokenError(HttpServletResponse response, Exception exception) throws IOException {
        response.setHeader("error", exception.getMessage());
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
    public void getLoggedUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String username = getUsernameFromJWT(authorizationHeader);
                getAuthorityClaimsFromJWT(authorizationHeader);
                User user = userService.getUser(username).get();
                UserDataDto userDataDto = new UserDataDto(user);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), userDataDto);
            } catch (Exception exception) {
                requestingNewAccessTokenError(response, exception);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
