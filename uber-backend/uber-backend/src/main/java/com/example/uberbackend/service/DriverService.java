package com.example.uberbackend.service;

import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.dto.RegisterDriverDto;
import com.example.uberbackend.exception.CustomValidationException;
import com.example.uberbackend.exception.EmailAlreadyTakenException;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.AccountStatus;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.Provider;
import com.example.uberbackend.model.enums.RoleType;
import com.example.uberbackend.repositories.*;
import com.example.uberbackend.security.SecurityConfig;
import com.example.uberbackend.validator.PasswordMatchValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.util.EmptyStackException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class DriverService {

    private final UserRepository userRepository;
    private final DriverInfoChangeRequestRepository driverInfoChangeRequestRepository;
    private final DriverRepository driverRepository;
    private final PointRepository pointRepository;

    public void updatePersonalInfo(PersonalInfoUpdateDto newInfo) {
        Optional<User> u = userRepository.findByEmail(newInfo.getEmail());

        if(u.isPresent()) {
            User toUpdate = u.get();
            PersonalInfoUpdateDto oldInfo = new PersonalInfoUpdateDto(toUpdate);

            DriverInfoChangeRequest di = new DriverInfoChangeRequest();
            di.setAccepted(false);
            di.setNewData(newInfo);
            di.setOldData(oldInfo);

            driverInfoChangeRequestRepository.save(di);
        }else {
            throw new UsernameNotFoundException("User with the given email does not exist!");
        }
    }


    public Driver updateDriverLocation(long id, double latitude, double longitude) {
        Optional<Driver> optDriver = this.driverRepository.findById(id);
        if(optDriver.isEmpty())
            throw new EmptyStackException();
        Driver driver = optDriver.get();
        Point newPoint = new Point();
        newPoint.setLat(latitude);
        newPoint.setLng(longitude);
        pointRepository.save(newPoint);

        driver.setCurrentLocation(newPoint);
        driverRepository.save(driver);

        return driver;
    }

    public Driver getDriver(long driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);

        return driver;
    }
}
