package com.example.uberbackend.service;

import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.model.DriverInfoChangeRequest;
import com.example.uberbackend.model.User;
import com.example.uberbackend.repositories.ClientRepository;
import com.example.uberbackend.repositories.DriverInfoChangeRequestRepository;
import com.example.uberbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class DriverService {

    private final UserRepository userRepository;
    private final DriverInfoChangeRequestRepository driverInfoChangeRequestRepository;

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
}
