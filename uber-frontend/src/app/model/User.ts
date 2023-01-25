export interface User{
    name: string,
    surname: string,
    email: string,
    role: string,
    city: string,
    phoneNumber: string,
    activeAccount: boolean,
    blocked: boolean,
    profileImage?: string,
    drivingStatus: string,
    accountStatus: string
  }