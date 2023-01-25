package com.example.uberbackend.service;

import com.example.uberbackend.dto.ChartDataDto;
import com.example.uberbackend.dto.ReportRequestDto;
import com.example.uberbackend.dto.ReportResponseDto;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.RoleType;
import com.example.uberbackend.repositories.RideRepository;
import com.example.uberbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final RideRepository rideRepository;
    private final MapService mapService;

    public ReportResponseDto generateReports(ReportRequestDto reportRequestDto){
        List<Ride> rideList = rideRepository.findAllEnded();

        ChartDataDto ridePerDay = generateRidesPerDay(reportRequestDto, rideList, false);
        ChartDataDto kmPerDay = null;
        try {
            kmPerDay = generateKmPerDay(reportRequestDto, rideList, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChartDataDto moneyPerDay = generateMoneyPerDay(reportRequestDto, rideList, false);

        ReportResponseDto reportResponseDto = new ReportResponseDto();
        reportResponseDto.setRidesPerDay(ridePerDay);
        reportResponseDto.setKmPerDay(kmPerDay);
        reportResponseDto.setMoneyPerDay(moneyPerDay);
        reportResponseDto.setAdminReport(null);

        if(reportRequestDto.getRoleType() == RoleType.ADMIN){
            ChartDataDto ridePerDayForAll = generateRidesPerDay(reportRequestDto, rideList, true);
            ChartDataDto kmPerDayForAll = null;
            try {
                kmPerDayForAll = generateKmPerDay(reportRequestDto, rideList, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChartDataDto moneyPerDayForAll = generateMoneyPerDay(reportRequestDto, rideList, true);

            ReportResponseDto adminReportResponse = new ReportResponseDto();
            adminReportResponse.setRidesPerDay(ridePerDayForAll);
            adminReportResponse.setKmPerDay(kmPerDayForAll);
            adminReportResponse.setMoneyPerDay(moneyPerDayForAll);
            adminReportResponse.setAdminReport(null);
            reportResponseDto.setAdminReport(adminReportResponse);
        }

        return reportResponseDto;
    }

    private ChartDataDto generateMoneyPerDay(ReportRequestDto reportRequestDto, List<Ride> rideList, boolean allUsers) {
        LocalDate startDate = reportRequestDto.getStart();
        LocalDate endDate = reportRequestDto.getEnd();
        ChartDataDto moneyPerDay = new ChartDataDto();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        for (; startDate.isBefore(endDate) || startDate.isEqual(endDate); startDate = startDate.plusDays(1)){
            double ridePrice = 0;
            labels.add(startDate.getDayOfMonth()+"."+ startDate.getMonthValue()+"."+ startDate.getYear()+".");
            for (Ride ride : rideList) {
                if(isUserInRide(ride, reportRequestDto, allUsers) && ride.getStartTime().toLocalDate().isEqual(startDate)) {
                    if(reportRequestDto.getRoleType() == RoleType.CLIENT) {
                        if (ride.getPrice() == ride.getPricePerPassenger()) {
                            if (ride.getInitiator().getEmail().equals(reportRequestDto.getUserEmail()))
                                ridePrice += ride.getPrice();
                        } else
                            ridePrice += ride.getPricePerPassenger();
                    }
                    else
                        ridePrice += ride.getPrice();
                }
            }
            values.add(ridePrice);
        }
        moneyPerDay.setLabels(labels);
        moneyPerDay.setValues(values);
        return moneyPerDay;
    }

    private boolean isUserInRide(Ride ride, ReportRequestDto reportRequestDto, boolean allUsers) {
        if(allUsers)
            return true;
        List<Client> clients = ride.getClients();
        clients.add(ride.getInitiator());
        Driver driver = ride.getDriver();
        return clients.stream().filter(client -> client.getEmail().equals(reportRequestDto.getUserEmail())).count() > 0
                || driver.getEmail().equals(reportRequestDto.getUserEmail());
    }

    private ChartDataDto generateKmPerDay(ReportRequestDto reportRequestDto, List<Ride> rideList, boolean allUsers) throws IOException {
        LocalDate startDate = reportRequestDto.getStart();
        LocalDate endDate = reportRequestDto.getEnd();
        ChartDataDto kmPerDayChart = new ChartDataDto();
        Map<Long, Double> rideDistanceMap = getRideDistances(rideList);

        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        for (; startDate.isBefore(endDate) || startDate.isEqual(endDate); startDate = startDate.plusDays(1)){
            double kmPerDay = 0;
            labels.add(startDate.getDayOfMonth()+"."+ startDate.getMonthValue()+"."+ startDate.getYear()+".");
            for (Ride ride : rideList) {
                if(isUserInRide(ride, reportRequestDto, allUsers)
                    && ride.getStartTime().toLocalDate().isEqual(startDate)){
                    double distance = rideDistanceMap.get(ride.getId());
                    kmPerDay += distance;
                }
            }
            values.add(kmPerDay);
        }
        kmPerDayChart.setLabels(labels);
        kmPerDayChart.setValues(values);
        return kmPerDayChart;
    }

    private Map<Long, Double> getRideDistances(List<Ride> rideList) throws IOException {
        Map<Long, Double> rideDistanceMap = new HashMap<>();
        for (Ride ride : rideList) {
            double distance = 0;
            List<Point> points = ride.getLocations()
                    .stream()
                    .map(c -> new Point(c.getLat(), c.getLon()))
                    .collect(Collectors.toList());
            switch (ride.getRouteType()){
                case "Custom":{
                    distance = mapService.getCustomRoute(points).getDistance();
                    break;
                }
                case "Alternative":{
                    distance = mapService.getAlternativeRoute(points).getDistance();
                    break;
                }
                default:
                    distance = mapService.getOptimalRoute(points).getDistance();
            }
            rideDistanceMap.put(ride.getId(), distance);
        }
        return rideDistanceMap;
    }

    private ChartDataDto generateRidesPerDay(ReportRequestDto reportRequestDto, List<Ride> rideList, boolean allUsers) {
        LocalDate startDate = reportRequestDto.getStart();
        LocalDate endDate = reportRequestDto.getEnd();
        ChartDataDto ridePerDay = new ChartDataDto();

        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        for (; startDate.isBefore(endDate) || startDate.isEqual(endDate); startDate = startDate.plusDays(1)){
            double dayRides = 0;
            labels.add(startDate.getDayOfMonth()+"."+ startDate.getMonthValue()+"."+ startDate.getYear()+".");
            for (Ride ride : rideList) {
                if(isUserInRide(ride, reportRequestDto, allUsers)
                && ride.getStartTime().toLocalDate().isEqual(startDate))
                    dayRides++;
            }
            values.add(dayRides);
        }
        ridePerDay.setLabels(labels);
        ridePerDay.setValues(values);
        return ridePerDay;
    }

}
