import { MapSearchResult } from '../modules/client/services/map.service';

export interface RideRequest{
    initiatorEmail: string,
    locations: MapSearchResult[],
    price: number,
    pricePerPassenger: number,
    vehicleType: string,
    routeType: string,
    people: string[],
    peopleLeftToRespond: string[],
    totalDistance: number,
    invitesSent: boolean,
    isReserved?: boolean,
    timeOfReservation?: Date,
    timeOfRequestForReservation?: Date
}