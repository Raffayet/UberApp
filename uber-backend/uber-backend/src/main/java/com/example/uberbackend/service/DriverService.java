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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private final RejectionRepository rejectionRepository;

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

        List<Driver> busyDrivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE_BUSY);
        Optional<Driver> closestToFinishDriver = findDriverClosestToFinish(busyDrivers, request);

        if(driver.isPresent()){
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverFound", "Driver " + driver.get().getEmail() + " has been found for your ride request."));
            return new DriverFoundDto(driver.get().getEmail(), true);
        }

        else if(closestToFinishDriver.isPresent())
        {
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverFound", "Driver " + closestToFinishDriver.get().getEmail() + " has been found for your ride request."));
            return new DriverFoundDto(closestToFinishDriver.get().getEmail(), true);
        }

        else{
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("noDrivers", "No available drivers. Please try later."));
            throw new NoAvailableDriversException("There is no available drivers!");
        }
    }

    private Optional<Driver> findDriverClosestToFinish(List<Driver> busyDrivers, DriveRequest request) throws IOException {
        Optional<Driver> closestToFinish = Optional.empty();
        double minDistanceToDestination = 9999;
        for(Driver driver: busyDrivers)
        {
            boolean hasReservedRides =  doesDriverHaveReservedRides(driver);
            if(!request.getDriversThatRejected().contains(driver) && driver.getDailyActiveInterval() <= 480 && !hasReservedRides)
            {
                if(calculateDistance(driver.getRides().get(0).getLocations().get(1), driver.getCurrentLocation()) < minDistanceToDestination)
                {
                    minDistanceToDestination = calculateDistance(driver.getRides().get(0).getLocations().get(1), driver.getCurrentLocation());
                    closestToFinish = Optional.of(driver);
                }
            }
        }
        return closestToFinish;
    }

    private boolean doesDriverHaveReservedRides(Driver driver) {
        boolean hasReservedRides = false;
        for(Ride ride: driver.getRides())
        {
            if (ride.getReserved() && ride.getTimeOfReservation().isAfter(LocalDateTime.now())) {
                hasReservedRides = true;
                break;
            }
        }
        return hasReservedRides;
    }

    private Optional<Driver> findClosestAvailableDriver(List<Driver> drivers, DriveRequest request) throws IOException {
        Optional<Driver> closest = Optional.empty();
        double minDistance = 9999;
        for(Driver d : drivers){
            if(!request.getDriversThatRejected().contains(d) && d.getDailyActiveInterval() <= 480){
                if(calculateDistance(request.getLocations().get(0), d.getCurrentLocation()) < minDistance) {
                    minDistance = calculateDistance(request.getLocations().get(0), d.getCurrentLocation());
                    closest = Optional.of(d);
                }
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
        RideToTakeDto rideToTakeDto = new RideToTakeDto(request.getId(), request.getLocations().get(0).getDisplayName(), request.getLocations().get(1).getDisplayName(), request.getInitiator().getEmail(), request.getIsReserved(), request.getTimeOfReservation());
        simpMessagingTemplate.convertAndSendToUser(driverFoundDto.getDriverEmail(), "/driver-notification", rideToTakeDto);
    }

    @Scheduled(cron = "0 0 0 * * *")
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
            simpMessagingTemplate.convertAndSendToUser(driveAssignatureDto.getDriverEmail(), "/change-driving-status-slider", "false");
            Map<String, Object> headers = generateNotificationHeaders(driveRequest.get());
            simpMessagingTemplate.convertAndSend("/topic/response-to-other-clients", new ResponseToIniciatorDto("driverAccepted", "Driver has accepted. Enjoy your ride!"), headers);
        }
    }

    private Map<String, Object> generateNotificationHeaders(DriveRequest driveRequest) {
        Map<String, Object> headers = new HashMap<>();
        List<String> clientsEmails = new ArrayList<String>();

        clientsEmails.add(driveRequest.getInitiator().getEmail());
        for (Client client : driveRequest.getPeople()) {
            clientsEmails.add(client.getEmail());
        }

        headers.put("emails", clientsEmails);
        return headers;
    }

    public void rejectDrive(DriverRejectionDto driverRejectionDto) {
        Optional<Driver> driver = this.driverRepository.findByEmail(driverRejectionDto.getDriverEmail());
        Optional<DriveRequest> driveRequest = this.driveRequestRepository.findById(driverRejectionDto.getRequestId());
        if(driveRequest.isPresent() && driver.isPresent())
        {
            driveRequest.get().getDriversThatRejected().add(driver.get());
            this.driveRequestRepository.save(driveRequest.get());
            simpMessagingTemplate.convertAndSendToUser(driveRequest.get().getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverRejected", "Driver " + driver.get().getEmail() + " has rejected this drive request. Reason: " + driverRejectionDto.getReasonForRejection()));
            for (Client client: driveRequest.get().getPeople())
            {
                simpMessagingTemplate.convertAndSendToUser(client.getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverRejected", "Driver " + driver.get().getEmail() + " has rejected this drive request. Reason: " + driverRejectionDto.getReasonForRejection()));
            }
        }
        createRejection(driverRejectionDto);
    }

    private void createRejection(DriverRejectionDto driverRejectionDto) {
        Rejection rejection = new Rejection();
        rejection.setDriverEmail(driverRejectionDto.getDriverEmail());
        rejection.setInitiatorEmail(driverRejectionDto.getInitiatorEmail());
        rejection.setRequestId(driverRejectionDto.getRequestId());
        rejection.setReasonOfRecetion(driverRejectionDto.getReasonForRejection());
        this.rejectionRepository.save(rejection);
    }
}
