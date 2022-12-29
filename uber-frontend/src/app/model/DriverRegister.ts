export interface DriverRegister{
    firstName:string,
    lastName:string,
    email:string,
    password:string,
    confirmPassword:string,
    city:string,
    telephone:string,
    vehicle:Vehicle
  }

interface Vehicle{
    model:string,
    vehicleType:string
}