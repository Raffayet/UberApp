package com.example.uberbackend.service;
import com.example.uberbackend.dto.*;
import com.example.uberbackend.exception.DriveRequestNotFoundException;
import com.example.uberbackend.exception.DriverNotFoundException;
import com.example.uberbackend.exception.NoAvailableDriversException;
import com.example.uberbackend.exception.RideNotFoundException;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.repositories.*;
import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.security.JwtTokenGenerator;
import com.example.uberbackend.task.DisableRatingScheduler;
import com.example.uberbackend.task.NotificationScheduler;
import com.example.uberbackend.task.ReservationScheduler;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.time.ZoneOffset;
import java.util.EmptyStackException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class DriverService {

    private final UserRepository userRepository;
    private final DriverInfoChangeRequestRepository driverInfoChangeRequestRepository;
    private final DriverRepository driverRepository;
    private final PointRepository pointRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DriveRequestRepository driveRequestRepository;
    private final RideRepository rideRepository;
    private final MapService mapService;
    private final RejectionRepository rejectionRepository;
    private final ClientRepository clientRepository;
    private final RatingRepository ratingRepository;
    private ThreadPoolTaskScheduler taskScheduler;
    private final JwtTokenGenerator tokenGenerator;

    public void updatePersonalInfo(PersonalInfoUpdateDto newInfo) {
        Optional<User> u = userRepository.findByEmail(newInfo.getEmail());

        if(u.isPresent()) {
            User toUpdate = u.get();
            PersonalInfoUpdateDto oldInfo = new PersonalInfoUpdateDto(toUpdate);

            DriverInfoChangeRequest di = new DriverInfoChangeRequest();
            di.setAccepted(false);
            di.setReviewed(false);
            di.setNewData(newInfo);
            di.setOldData(oldInfo);

            driverInfoChangeRequestRepository.save(di);
            return;
        }
        throw new UsernameNotFoundException("User with the given email does not exist!");
    }

    public DriverFoundDto findDriverForRequest(DriveRequest request) throws IOException {
        List<Driver> availableDrivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE);
        availableDrivers = availableDrivers.stream().filter(avDriver -> !avDriver.getBlocked()).collect(Collectors.toList());
        Optional<Driver> driver = findClosestAvailableDriver(availableDrivers, request);

        if(driver.isPresent()){
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverFound", "Driver " + driver.get().getEmail() + " has been found for your ride request."));
            return new DriverFoundDto(driver.get().getEmail(), true);
        }

        List<Driver> busyDrivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE_BUSY);
        busyDrivers = busyDrivers.stream().filter(buDriver -> !buDriver.getBlocked()).collect(Collectors.toList());
        Optional<Driver> closestToFinishDriver = findDriverClosestToFinish(busyDrivers, request);

        if(closestToFinishDriver.isPresent())
        {
            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverFound", "Driver " + closestToFinishDriver.get().getEmail() + " has been found for your ride request."));
            return new DriverFoundDto(closestToFinishDriver.get().getEmail(), true);
        }

        simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("noDrivers", "No available drivers. Please try later."));
        throw new NoAvailableDriversException("There is no available drivers!");
    }

    private Optional<Driver> findDriverClosestToFinish(List<Driver> busyDrivers, DriveRequest request) throws IOException {
        Optional<Driver> closestToFinish = Optional.empty();
        double minDistanceToDestination = 9999;
        for(Driver driver: busyDrivers)
        {
            boolean hasReservedRides = doesDriverHaveReservedRides(driver);
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
        double minDistance = Double.POSITIVE_INFINITY;
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
        if(driver.isEmpty())
            throw new DriverNotFoundException();

        if(driveRequest.isEmpty())
            throw new DriveRequestNotFoundException();

        Ride ride = new Ride(driveRequest.get(), driver.get());
        ride.setRideStatus(RideStatus.WAITING);
        this.rideRepository.save(ride);
        driver.get().getRides().add(ride);
        driver.get().setDrivingStatus(DrivingStatus.ONLINE_BUSY);
        this.driverRepository.save(driver.get());
        simpMessagingTemplate.convertAndSendToUser(driveAssignatureDto.getInitiatorEmail(), "/response-to-client", new ResponseToIniciatorDto("driverAccepted", "Driver has accepted. Enjoy your ride!"));
        simpMessagingTemplate.convertAndSendToUser(driveAssignatureDto.getDriverEmail(), "/change-driving-status-slider", "false");
        Map<String, Object> headers = generateNotificationHeaders(driveRequest.get());
        simpMessagingTemplate.convertAndSend("/topic/response-to-other-clients", new ResponseToIniciatorDto("driverAccepted", "Driver has accepted. Enjoy your ride!"), headers);
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
        Optional<Driver> driver = Optional.ofNullable(this.driverRepository.findByEmail(driverRejectionDto.getDriverEmail()).orElseThrow(DriverNotFoundException::new));
        Optional<DriveRequest> driveRequest = Optional.ofNullable(this.driveRequestRepository.findById(driverRejectionDto.getRequestId()).orElseThrow(DriveRequestNotFoundException::new));

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

    public void rejectDriveAfterAccepting(DriverRejectionDto driverRejectionDto) {
        Driver driver = this.driverRepository.findByEmail(driverRejectionDto.getDriverEmail()).orElseThrow(DriverNotFoundException::new);
        Ride ride = this.rideRepository.findById(driverRejectionDto.getRequestId()).orElseThrow(RideNotFoundException::new);


        removeRide(driver, ride);
        simpMessagingTemplate.convertAndSendToUser(ride.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverRejected", "Driver " + driver.getEmail() + " has rejected this drive request. Reason: " + driverRejectionDto.getReasonForRejection()));
        for (Client client: ride.getClients())
        {
            simpMessagingTemplate.convertAndSendToUser(client.getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverRejected", "Driver " + driver.getEmail() + " has rejected this drive request. Reason: " + driverRejectionDto.getReasonForRejection()));
        }
        createRejection(driverRejectionDto);
    }

    private void removeRide(Driver driver, Ride ride) {
        List<Ride> rides = driver.getRides();
        rides.removeIf(driversRide -> Objects.equals(driversRide.getId(), ride.getId()));
        driver.setRides(rides);
        this.driverRepository.save(driver);
        ride.setRideStatus(RideStatus.CANCELED);
        this.rideRepository.save(ride);
    }

    private void createRejection(DriverRejectionDto driverRejectionDto) {
        Rejection rejection = new Rejection();
        rejection.setDriverEmail(driverRejectionDto.getDriverEmail());
        rejection.setInitiatorEmail(driverRejectionDto.getInitiatorEmail());
        rejection.setRequestId(driverRejectionDto.getRequestId());
        rejection.setReasonOfRecetion(driverRejectionDto.getReasonForRejection());
        this.rejectionRepository.save(rejection);
    }

    public Driver updateDriverLocation(long id, double latitude, double longitude) {
        Driver driver = this.driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);

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
    public List<RideToShowDto> findAllRidesToDo(String driverEmail) {
        List<RideToShowDto> ridesToShowDto = new ArrayList<RideToShowDto>();
        Optional<Driver> driver = this.driverRepository.findByEmail(driverEmail);
        if(driver.isPresent())
        {
            for(int i = 0; i < driver.get().getRides().size(); i++)
            {
                RideToShowDto rideToShowDto = new RideToShowDto(driver.get().getRides().get(i));
                ridesToShowDto.add(rideToShowDto);
            }
        }

        return ridesToShowDto;
    }

    public DriverInfoDto getDriverInfoByRideId(Long rideId) {
        Optional<Ride> ride = this.rideRepository.findById(rideId);
        DriverInfoDto driverInfoDto = new DriverInfoDto();
        if(ride.isPresent())
        {
            Driver driver = ride.get().getDriver();
            driverInfoDto.setEmail(driver.getEmail());
            driverInfoDto.setName(driver.getName());
            driverInfoDto.setLastName(driver.getSurname());
            calculateAverageDriverRating(driverInfoDto, driver);
        }
        return driverInfoDto;
    }

    private void calculateAverageDriverRating(DriverInfoDto driverInfoDto, Driver driver) {
        double averageRating = 0;
        for(Rating rating: driver.getRatingsFromClients())
        {
            averageRating += rating.getStarNumber();
        }

        if(driver.getRatingsFromClients().size() == 0)
        {
            averageRating = 0;
        }

        else
        {
            averageRating = averageRating / driver.getRatingsFromClients().size();
        }
        driverInfoDto.setAverageRating(averageRating);
    }

    public List<MapDriverDto> getActiveDrivers() {
        List<Driver> activeDrivers = driverRepository.findAllOnline();
        List<MapDriverDto> mapDriverDtos = new ArrayList<>();
        for (Driver driver : activeDrivers) {
            mapDriverDtos.add(new MapDriverDto(driver));
        }
        return mapDriverDtos;
    }

    public void rateDriver(RateDriverDto rateDriverDto) {
        Optional<Driver> driver = this.driverRepository.findByEmail(rateDriverDto.getDriverEmail());
        Optional<Client> client = this.clientRepository.findByEmail(rateDriverDto.getClientEmail());

        if(driver.isPresent() && client.isPresent())
        {
            Rating rating = new Rating();
            rating.setStarNumber(rateDriverDto.getNumberOfStars());
            rating.setComment(rateDriverDto.getComment());
            rating.setDriver(driver.get());
            rating.setClient(client.get());
            this.ratingRepository.save(rating);
            driver.get().getRatingsFromClients().add(rating);
            this.driverRepository.save(driver.get());
        }
    }

    public List<Driver> getAllRatedDrivers(String clientEmail) {
        List<Driver> allRatedDrivers = new ArrayList<Driver>();
        List<Rating> allRatings = this.ratingRepository.findAll();
        for(Rating rating: allRatings)
        {
            if(rating.getClient().getEmail().equals(clientEmail) && !allRatedDrivers.contains(rating.getDriver()))
            {
                allRatedDrivers.add(rating.getDriver());
            }
        }
        return allRatedDrivers;
    }

    public List<Long> findRideIdsByDriver(List<Driver> allRatedDrivers) {
        List<Long> alreadyRatedRideIds = new ArrayList<>();
        for(Driver driver: allRatedDrivers)
        {
            for(Ride ride: driver.getRides())
            {
                alreadyRatedRideIds.add(ride.getId());
            }
        }
        return alreadyRatedRideIds;
    }

    public String getDriverEmailById(Long driverId) {
        Optional<Driver> driver = this.driverRepository.findById(driverId);
        String driverEmail = "";
        if(driver.isPresent())
        {
            driverEmail = driver.get().getEmail();
        }
        return driverEmail;
    }

    public void setRatingExpiration(Long rideId) {
        Optional<Ride> ride = this.rideRepository.findById(rideId);
        LocalDateTime scheduledFor = LocalDateTime.now();
        ride.ifPresent(value -> taskScheduler.schedule(new DisableRatingScheduler(value, this.simpMessagingTemplate, this.rideRepository), scheduledFor.minusHours(1).plusDays(3).toInstant(ZoneOffset.UTC)));
    }

    public List<DriverInfoChangeRequest> getDriverInfoChangeRequests() {
        List<DriverInfoChangeRequest> driverInfoChangeRequests = driverInfoChangeRequestRepository.findAll();
        driverInfoChangeRequests = driverInfoChangeRequests.stream().filter(di-> !di.getReviewed()).collect(Collectors.toList());
        return driverInfoChangeRequests;
    }

    public DriverInfoChangeResponse respondToInfoRequest(DriverInfoChangeRequest dto) {
        DriverInfoChangeResponse driverInfoChangeResponse = new DriverInfoChangeResponse();

        Optional<DriverInfoChangeRequest> driverInfoChangeRequest = driverInfoChangeRequestRepository.findById(dto.getId());
        if(driverInfoChangeRequest.isPresent()){
            DriverInfoChangeRequest diRequest = driverInfoChangeRequest.get();
            diRequest.setReviewed(true);
            diRequest.setAccepted(dto.getAccepted());
            driverInfoChangeRequestRepository.save(diRequest);
            driverInfoChangeResponse.setAccepted(dto.getAccepted());
            driverInfoChangeResponse.setId(dto.getId());


            if(dto.getAccepted()) {
                User user = userRepository.findByEmail(dto.getOldData().getEmail()).orElse(null);
                if (user != null) {
                    user.setName(dto.getNewData().getName());
                    user.setSurname(dto.getNewData().getSurname());
                    user.setCity(dto.getNewData().getCity());
                    user.setPhoneNumber(dto.getNewData().getPhone());
                    userRepository.save(user);
                    driverInfoChangeResponse.setToken(tokenGenerator.generateToken(user));
                    return driverInfoChangeResponse;
                }
            }
            else
                return driverInfoChangeResponse;
        }
        throw new NoSuchElementException("No driver info change requests");
    }
}
