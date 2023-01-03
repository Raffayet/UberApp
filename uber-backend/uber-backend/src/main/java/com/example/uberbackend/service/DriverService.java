package com.example.uberbackend.service;
import com.example.uberbackend.dto.*;
import com.example.uberbackend.exception.NoAvailableDriversException;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.repositories.*;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private final MapService mapService;

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

    public DriverFoundDto findDriverForRequest(DriveRequest request) throws IOException {
        List<Driver> availableDrivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE);

        Optional<Driver> driver = findClosestAvailableDriver(availableDrivers, request);
        if(driver.isPresent()){
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverFound", "Driver " + driver.get().getEmail() + " has been found for your ride request."));
            return new DriverFoundDto(driver.get().getEmail(), true);
        }else{
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("noDrivers", "No available drivers. Please try later."));
            throw new NoAvailableDriversException("There is no available drivers!");
        }
    }

    private Optional<Driver> findClosestAvailableDriver(List<Driver> drivers, DriveRequest request) throws IOException {
        Optional<Driver> closest = Optional.empty();
        double minDistance = 9999;
        for(Driver d : drivers){
            if(!request.getDriversThatRejected().contains(d) && d.getDailyActiveInterval() <= 480){
                minDistance = Math.min(minDistance, calculateDistance(request.getLocations().get(0), d.getCurrentLocation()));
                closest = Optional.of(d);
            }
        }

        return closest;
    }

    private double calculateDistance(MapSearchResultDto mapSearchResultDto, Point driversLocation) throws IOException {
        List<Point> points = new ArrayList<>();
        points.add(new Point(mapSearchResultDto.getLat(), mapSearchResultDto.getLon()));
        points.add(driversLocation);

        PathInfoDto dto = this.mapService.getOptimalRoute(points);
        return dto.getDistance();
    }

    public void sendRequestToDriver(DriveRequest request, DriverFoundDto driverFoundDto) {
        RideToTakeDto rideToTakeDto = new RideToTakeDto(request.getId(), request.getLocations().get(0).getDisplayName(), request.getLocations().get(1).getDisplayName(), request.getInitiator().getEmail(), request.getIsReserved());
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
            driver.get().setDrivingStatus(DrivingStatus.ONLINE_BUSY);
            this.driverRepository.save(driver.get());
            simpMessagingTemplate.convertAndSendToUser(driveAssignatureDto.getInitiatorEmail(), "/response-to-client", new ResponseToIniciatorDto("driverAccepted", "Driver has accepted. Enjoy your ride!"));

            sendResponseToClients(driveRequest.get());
        }
    }

    private void sendResponseToClients(DriveRequest driveRequest) {
        for(Client client: driveRequest.getPeople())
        {
            simpMessagingTemplate.convertAndSendToUser(client.getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverAccepted", "Driver has accepted. Enjoy your ride!"));
        }
    }


}
