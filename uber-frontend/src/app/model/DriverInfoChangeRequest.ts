export interface DriverChangeRequest{
    id:number,
    accepted:boolean,
    reviewed:boolean,
    oldData:PersonalInfoUpdate,
    newData:PersonalInfoUpdate
}

interface PersonalInfoUpdate{
    id:number,
    email:string,
    name:string,
    surname:string,
    role:string,
    city:string,
    phone:string
}