export interface MapRide{
    id:number,
    status:string,
    driver:MapDriver,
    clientEmails:string[],
    atomicPoints:AtomicPoint[],
    atomicPointsBeforeRide:AtomicPoint[],
    duration:number
}

export interface MapDriver{
    id:number,
    longitude:number,
    latitude:number
}

export interface AtomicPoint{
    latitude:number,
    longitude:number
}