package com.example.uberbackend.service;

import com.example.uberbackend.dto.DriverNotificationDto;
import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.exception.NoAvailableDriversException;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.DriverInfoChangeRequest;
import com.example.uberbackend.model.User;
import com.example.uberbackend.repositories.ClientRepository;
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
        List<Driver> availableDrivers = driverRepository.findAvailableDrivers();
        boolean found = false;
        for(Driver d : availableDrivers){
            if(!request.getDriversThatRejected().contains(d)){
                found = true;
                // send driver notification via socket with this request
                String email = d.getEmail();
                simpMessagingTemplate.convertAndSendToUser(email, "/driver-notification", new DriverNotificationDto(request.getId(), request.getLocations().get(0)));
            }
        }
        if(!found){ // svi su zauzeti
            throw new NoAvailableDriversException("Currently, there are no available drivers.");
        }
    }
}
