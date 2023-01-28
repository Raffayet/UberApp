package com.example.uberbackend.unit;

import com.example.uberbackend.dto.LocationDto;
import com.example.uberbackend.dto.MapDriverDto;
import com.example.uberbackend.dto.MapRideDto;
import com.example.uberbackend.exception.DriverNotFoundException;
import com.example.uberbackend.exception.NoVehicleTypesException;
import com.example.uberbackend.exception.NotEnoughPointsForRouteException;
import com.example.uberbackend.exception.RideNotFoundException;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.repositories.DriverRepository;
import com.example.uberbackend.repositories.RideRepository;
import com.example.uberbackend.repositories.VehicleTypeRepository;
import com.example.uberbackend.service.RideService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class RideServiceTests {

    @InjectMocks
    RideService rideService;

    @Mock
    RideRepository rideRepository;

    @Mock
    DriverRepository driverRepository;

    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    VehicleTypeRepository vehicleTypeRepository;


    // EndRide - SW-1-2019
    @Test
    public void shouldEndRideSuccessfullyTest(){
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDrivingStatus(DrivingStatus.ONLINE_BUSY);
        Mockito.when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));

        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.STARTED);
        ride.setDriver(driver);
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));

        List<Ride> rides = new ArrayList<>();
        rides.add(ride);
        driver.setRides(rides);

        Ride r = rideService.endRide(123L);
        Assertions.assertEquals(DrivingStatus.ONLINE, r.getDriver().getDrivingStatus());
        verify(rideRepository, times(1)).save(any(Ride.class));
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    public void shouldThrowRideNotFoundExceptionInEndRideTest(){
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDrivingStatus(DrivingStatus.ONLINE_BUSY);
        Mockito.when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));

        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.STARTED);
        ride.setDriver(driver);
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.empty());

        List<Ride> rides = new ArrayList<>();
        rides.add(ride);
        driver.setRides(rides);

        Assertions.assertThrows(RideNotFoundException.class,
                () -> rideService.endRide(123L));
        verify(rideRepository, times(0)).save(any(Ride.class));
        verify(driverRepository, times(0)).save(any(Driver.class));
    }

    @Test
    public void shouldThrowDriverNotFoundExceptionInEndRideTest(){
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDrivingStatus(DrivingStatus.ONLINE_BUSY);
        Mockito.when(driverRepository.findById(anyLong())).thenReturn(Optional.empty());

        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.STARTED);
        ride.setDriver(driver);
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));

        List<Ride> rides = new ArrayList<>();
        rides.add(ride);
        driver.setRides(rides);

        Assertions.assertThrows(DriverNotFoundException.class,
                () -> rideService.endRide(123L));
        verify(rideRepository, times(1)).save(any(Ride.class));
        verify(driverRepository, times(0)).save(any(Driver.class));
    }

    @Test
    public void shouldHaveNextRideForDriverInEndRideTest(){
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDrivingStatus(DrivingStatus.ONLINE_BUSY);
        Mockito.when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));

        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.STARTED);
        ride.setDriver(driver);
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));

        Ride nextRide = new Ride();
        nextRide.setRideStatus(RideStatus.WAITING);
        List<Ride> rides = new ArrayList<>();
        rides.add(nextRide);
        driver.setRides(rides);

        Ride r = rideService.endRide(123L);

        verify(rideRepository, times(1)).save(any(Ride.class));
        verify(driverRepository, times(0)).save(any(Driver.class));
    }

    // CheckIfRideIsCancelled - SW-1-2019
    @Test
    public void shouldReturnRideIsNotCancelledTest(){
        MapRideDto dto = new MapRideDto();
        dto.setId(123L);

        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.STARTED);
        Mockito.when(rideRepository.findById(123L)).thenReturn(Optional.of(ride));

        boolean isCancelled = rideService.checkIfRideIsCanceled(dto);
        Assertions.assertFalse(isCancelled);

        verify(simpMessagingTemplate, times(0)).convertAndSend(anyString(), any(MapDriverDto.class));
    }

    @Test
    public void shouldReturnRideIsCancelledTest(){
        MapRideDto dto = new MapRideDto();
        dto.setId(123L);

        Driver driver = new Driver();
        driver.setId(1L);
        driver.setCurrentLocation(new Point(39.99, 39.00));

        Ride ride = new Ride();
        ride.setDriver(driver);
        ride.setRideStatus(RideStatus.CANCELED);
        Mockito.when(rideRepository.findById(123L)).thenReturn(Optional.of(ride));

        boolean isCancelled = rideService.checkIfRideIsCanceled(dto);
        Assertions.assertTrue(isCancelled);

        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(MapDriverDto.class));
    }

    @Test
    public void shouldThrowRideNotFoundExceptionWhenCheckingIsCancelledTest(){
        MapRideDto dto = new MapRideDto();
        dto.setId(123L);

        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.CANCELED);
        Mockito.when(rideRepository.findById(123L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RideNotFoundException.class,
                () -> rideService.checkIfRideIsCanceled(dto));
    }

    // UpdateRideStatus - SW-1-2019
    @Test
    public void shouldThrowNullPointerExceptionForAtomicPointsInUpdateRideStatusTest(){
        Assertions.assertThrows(NullPointerException.class,
                () -> rideService.updateRideStatus(new MapRideDto()));
    }

    @Test
    public void shouldThrowIndexOutOfBoundsExceptionForAtomicPointsInUpdateRideStatusTest(){
        MapRideDto dto = new MapRideDto();
        dto.setAtomicPoints(new ArrayList<>());
        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> rideService.updateRideStatus(dto));
    }

    @Test
    public void shouldThrowRideNotFoundExceptionInUpdateRideStatusTest(){
        MapDriverDto driver = new MapDriverDto();
        driver.setId(1L);
        driver.setLatitude(45.99);
        driver.setLongitude(19.85);

        MapRideDto dto = new MapRideDto();
        dto.setId(123L);
        dto.setDriver(driver);
        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(45.99, 19.85));
        dto.setAtomicPoints(points);

        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(RideNotFoundException.class,
                () -> rideService.updateRideStatus(dto));
    }

    @Test
    public void shouldUpdateRideStatusSuccessfullyTest(){
        MapDriverDto driver = new MapDriverDto();
        driver.setId(1L);
        driver.setLatitude(45.99);
        driver.setLongitude(19.85);

        MapRideDto dto = new MapRideDto();
        dto.setId(123L);
        dto.setDriver(driver);
        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(45.99, 19.85));
        dto.setAtomicPoints(points);

        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(new Ride()));

        Ride actual = rideService.updateRideStatus(dto);
        Assertions.assertEquals(RideStatus.STARTED, actual.getRideStatus());
        verify(rideRepository, times(1)).save(any(Ride.class));
    }

    @Test
    public void shouldNotUpdateRideStatusSuccessfullyTest(){
        MapDriverDto driver = new MapDriverDto();
        driver.setId(1L);
        driver.setLatitude(45.99);
        driver.setLongitude(19.85);

        MapRideDto dto = new MapRideDto();
        dto.setId(123L);
        dto.setDriver(driver);
        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(45.79, 19.95));
        dto.setAtomicPoints(points);

        Ride r = new Ride();
        r.setRideStatus(RideStatus.WAITING);

        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(r));

        Ride actual = rideService.updateRideStatus(dto);
        Assertions.assertEquals(RideStatus.WAITING, actual.getRideStatus());
        verify(rideRepository, times(0)).save(any(Ride.class));
    }



    @Test
    void calculatePriceSuccessTest(){
        String vehicleTypeString = "Standard";
        double totalDistance = 11;

        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(1L);
        vehicleType.setType("Standard");
        vehicleType.setCoefficient(1D);

        Mockito.when(vehicleTypeRepository.findByType(vehicleTypeString)).thenReturn(Optional.of(vehicleType));
        Assertions.assertEquals( 1.38, rideService.calculatePrice(vehicleTypeString, totalDistance));
    }

    @Test
    void calculatePriceNoVehicleTypeExTest(){
        String vehicleTypeString = "Standard";
        double totalDistance = 11;

        Mockito.when(vehicleTypeRepository.findByType(vehicleTypeString)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoVehicleTypesException.class, ()->rideService.calculatePrice(vehicleTypeString, totalDistance));
    }

    @Test
    void aproxDurationTestSuccessWaitingStatus()
    {
        MapRideDto mapRideDto = new MapRideDto();
        mapRideDto.setStatus(RideStatus.WAITING);

        List<LocationDto> atomicPointsBeforeRide = Arrays.asList(
                new LocationDto(45.18, 19.23),
                new LocationDto(45.18, 19.23)
        );
        mapRideDto.setAtomicPointsBeforeRide(atomicPointsBeforeRide);

        List<LocationDto> atomicPoints = Arrays.asList(
                new LocationDto(45.28, 19.23),
                new LocationDto(45.18, 19.33)
        );
        mapRideDto.setAtomicPoints(atomicPoints);
        mapRideDto.setDriver(new MapDriverDto(1, 45.23, 19.45));
        int expectedDuration = 3;
        rideService.aproxDuration(mapRideDto);
        Assertions.assertNotEquals(expectedDuration, mapRideDto.getDuration());
    }

    @Test
    void aproxDurationTestSuccessNotWaitingStatus()
    {
        MapRideDto mapRideDto = new MapRideDto();
        mapRideDto.setStatus(RideStatus.STARTED);

        List<LocationDto> atomicPointsBeforeRide = Arrays.asList(
                new LocationDto(45.18, 19.23),
                new LocationDto(45.18, 19.23)
        );
        mapRideDto.setAtomicPointsBeforeRide(atomicPointsBeforeRide);

        List<LocationDto> atomicPoints = Arrays.asList(
                new LocationDto(45.28, 19.23),
                new LocationDto(45.18, 19.33)
        );
        mapRideDto.setAtomicPoints(atomicPoints);
        mapRideDto.setDriver(new MapDriverDto(1, 45.23, 19.45));
        int expectedDuration = 3;
        rideService.aproxDuration(mapRideDto);
        Assertions.assertNotEquals(expectedDuration, mapRideDto.getDuration());
    }

    @Test
    void aproxDurationTestSuccessZeroDistance()
    {
        MapRideDto mapRideDto = new MapRideDto();
        mapRideDto.setStatus(RideStatus.STARTED);

        List<LocationDto> atomicPointsBeforeRide = new ArrayList<>();
        mapRideDto.setAtomicPointsBeforeRide(atomicPointsBeforeRide);

        List<LocationDto> atomicPoints = new ArrayList<>();
        mapRideDto.setAtomicPoints(atomicPoints);
        int expectedDuration = 3;
        rideService.aproxDuration(mapRideDto);
        Assertions.assertNotEquals(expectedDuration, mapRideDto.getDuration());
        Assertions.assertEquals(mapRideDto.getDuration(), 0);
    }
}
