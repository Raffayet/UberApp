package com.example.uberbackend.controller;

import com.example.uberbackend.model.Ride;
import com.example.uberbackend.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @GetMapping("get-all")
    public Page<Ride> getRides(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable paging = PageRequest.of(page, size);
        return rideService.findAll(paging);
    }
}
