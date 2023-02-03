export interface DriverRejection{
    requestId: number,
    driverEmail: string,
    initiatorEmail: string,
    reasonForRejection: string
}