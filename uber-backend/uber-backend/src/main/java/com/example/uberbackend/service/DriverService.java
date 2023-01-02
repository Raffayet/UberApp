package com.example.uberbackend.service;
import com.example.uberbackend.dto.*;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final DriveRequestRepository driveRequestRepository;
    private final RideRepository rideRepository;

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

    public DriverFoundDto findDriverForRequest(DriveRequest request){
        List<Driver> availableDrivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE);
        boolean found = false;
        String foundDriverEmail = "";
        for(Driver d : availableDrivers){
            if(!request.getDriversThatRejected().contains(d) && d.getDailyActiveInterval() <= 480){
                found = true;
                foundDriverEmail = d.getEmail();
                simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/good-response-to-iniciator", new ResponseToIniciatorDto("success", "Driver " + d.getEmail() + " has been assigned to your ride request."));
            }
        }
        if(!found){ // svi su zauzeti
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/bad-response-to-iniciator", new ResponseToIniciatorDto("error", "No available drivers. Please try later."));
        }

        return new DriverFoundDto(foundDriverEmail, found);
    }

    public void sendRequestToDriver(DriveRequest request, DriverFoundDto driverFoundDto) {
        RideToTakeDto rideToTakeDto = new RideToTakeDto(request.getId(), request.getLocations().get(0).getDisplayName(), request.getLocations().get(1).getDisplayName(), request.getInitiator().getEmail());
        simpMessagingTemplate.convertAndSendToUser(driverFoundDto.getDriverEmail(), "/driver-notification", rideToTakeDto);
    }

//    @Scheduled(cron = "0 12 * * ?")
    public void resetDailyWorkingInterval()
    {
        List<Driver> allDrivers = this.driverRepository.findAll();
        for(Driver driver: allDrivers)
        {
            driver.setDailyActiveInterval(0.0);
            this.driverRepository.save(driver);
        }
    }

    public void resetAfterLogout(String driverEmail) {
        Optional<Driver> driver = this.driverRepository.findByEmail(driverEmail);
        if(driver.isPresent())
        {
            double workingTime = ChronoUnit.MINUTES.between(driver.get().getLastTimeOfLogin(), LocalDateTime.now());
            double previousWorkingTime = driver.get().getDailyActiveInterval();
            driver.get().setDailyActiveInterval(previousWorkingTime + workingTime);
            this.driverRepository.save(driver.get());
        }
    }

    public void assignDriveToDriver(DriveAssignatureDto driveAssignatureDto) {
        Optional<Driver> driver = this.driverRepository.findByEmail(driveAssignatureDto.getDriverEmail());
        Optional<DriveRequest> driveRequest = this.driveRequestRepository.findById(driveAssignatureDto.getRequestId());
        if(driveRequest.isPresent() && driver.isPresent())
        {
            Ride ride = new Ride(driveRequest.get(), driver.get());
            this.rideRepository.save(ride);
            driver.get().getRides().add(ride);
            this.driverRepository.save(driver.get());
        }
    }
}
