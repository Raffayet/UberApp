package com.example.uberbackend.controller;
import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.model.Point;
import com.example.uberbackend.security.JwtTokenGenerator;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.MapService;
import com.example.uberbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @PostMapping(value = "/determine-route")
    public ResponseEntity<String> determineRoute(@RequestBody List<Point> points){
        try{
            String res = mapService.getRoute(points);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }
}
