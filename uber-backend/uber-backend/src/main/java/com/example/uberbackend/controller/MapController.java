package com.example.uberbackend.controller;
import com.example.uberbackend.dto.*;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.Point;
import com.example.uberbackend.security.JwtTokenGenerator;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.MapService;
import com.example.uberbackend.service.RideService;
import com.example.uberbackend.service.UserService;
import com.example.uberbackend.validator.ValidList;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DriverService driverService;
    private final RideService rideService;

    @PostMapping(value = "/determine-optimal-route")
    public ResponseEntity<?> determineOptimalRoute(@RequestBody @Valid ValidList<Point> points, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        PathInfoDto response = mapService.getOptimalRoute(points);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/determine-alternative-route")
    public ResponseEntity<?> determineAlternativeRoute(@RequestBody @Valid ValidList<Point> points, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        PathInfoDto response = mapService.getAlternativeRoute(points);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/determine-custom-route")
    public ResponseEntity<?> determineCustomRoute(@RequestBody @Valid ValidList<Point> points, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        PathInfoDto response = mapService.getCustomRoute(points);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(
            path = "/",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> updateDriverOnMap(@RequestBody @Valid MapRideDto mapRideDto, BindingResult result){
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }

        Driver driver = driverService.updateDriverLocation(mapRideDto.getDriver().getId(), mapRideDto.getDriver().getLatitude(), mapRideDto.getDriver().getLongitude());
        rideService.updateRideStatus(mapRideDto);
        rideService.checkIfRideIsCanceled(mapRideDto);
        rideService.aproxDuration(mapRideDto);

        this.simpMessagingTemplate.convertAndSend("/map-updates/update-ride-state-unauth", mapRideDto.getDriver());

        for (String email : mapRideDto.getClientEmails()) {
            simpMessagingTemplate.convertAndSendToUser(email, "/map-updates/update-ride-state", mapRideDto);
        }
        return new ResponseEntity<>(mapRideDto, HttpStatus.OK);
    }

}
