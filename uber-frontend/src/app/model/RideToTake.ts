export interface RideToTake {
    requestId: number,
    firstLocation: string,
    destination: string,
    initiatorEmail: string,
    isReserved: boolean,
    drivingTime?: Date
}