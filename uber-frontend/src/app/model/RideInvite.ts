export interface RideInvite {
    id?: number,
    emailFrom: string,
    emailsTo: string[],
    firstLocation: string,
    destination: string,
    rideInviteStatus: number,
    priceToPay: number
}