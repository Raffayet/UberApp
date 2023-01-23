import { MapSearchResult } from "../modules/client/services/map.service";
import { User } from "./User";

export interface Ride{
    id: number,
    driver: User,
    clients: User[],
    initiator: User,
    price: number,
    pricePerPassenger: number,
    locations: MapSearchResult[],
    routeType: string,
    startTime: string,
    endTime: string,
    formattedStartTime: string,
    formattedEndTime: string,
    vehicleType: string
}