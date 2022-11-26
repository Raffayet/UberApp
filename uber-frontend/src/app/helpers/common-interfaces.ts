export interface Message{
    senderEmail: string,
    senderFirstName: string | null,
    senderLastName: string | null,
    receiverEmail: string,
    receiverFirstName: string | null,
    receiverLastName: string | null,
    content: string,
    date: string,
    status: string
  }

  export interface User{
    name: string,
    surname: string,
    email: string,
    role: string,
    city: string,
    phoneNumber: string,
    activeAccount: boolean,
    blocked: boolean,
    profileImage: string,
    drivingStatus: string,
    accountStatus: string
  }