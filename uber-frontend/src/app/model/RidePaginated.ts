import { Ride } from "./Ride";

export interface RidePaginated {
    content: Ride,
    page: number,
    size: number
}