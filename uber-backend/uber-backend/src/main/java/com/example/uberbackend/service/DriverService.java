package com.example.uberbackend.service;

import com.example.uberbackend.dto.ResponseToIniciatorDto;
import com.example.uberbackend.dto.RideToTakeDto;
import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.exception.NoAvailableDriversException;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.DriverInfoChangeRequest;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.repositories.DriverInfoChangeRequestRepository;
import com.example.uberbackend.repositories.DriverRepository;
import com.example.uberbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class DriverService {

    private final UserRepository userRepository;
    private final DriverInfoChangeRequestRepository driverInfoChangeRequestRepository;
    private final DriverRepository driverRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

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
        }
        throw new UsernameNotFoundException("User with the given email does not exist!");
    }

    public void findDriverForRequest(DriveRequest request){
        List<Driver> availableDrivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE);
        boolean found = false;
        for(Driver d : availableDrivers){
            if(!request.getDriversThatRejected().contains(d)){
                found = true;
                // send driver notification v
                // ia socket with this request
                String email = d.getEmail();
                RideToTakeDto rideToTakeDto = new RideToTakeDto(request.getLocations().get(0).getDisplayName(), request.getLocations().get(1).getDisplayName(), request.getInitiator().getEmail());
                simpMessagingTemplate.convertAndSendToUser(email, "/driver-notification", rideToTakeDto);       //ova notifikacija treba kasnije da se posalje kad se uspesno izvrsi transakcija
                simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/good-response-to-iniciator", new ResponseToIniciatorDto("success", "Driver " + d.getEmail() + " has been assigned to your ride request."));
            }
        }
        if(!found){ // svi su zauzeti
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/bad-response-to-iniciator", new ResponseToIniciatorDto("error", "No available drivers. Please try later."));
        }
    }
}
