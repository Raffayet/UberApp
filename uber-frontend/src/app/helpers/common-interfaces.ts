export interface Message{
    senderEmail: string,
    senderFirstName: string | null,
    senderLastName: string | null,
    senderImage: string | null,
    receiverEmail: string,
    receiverFirstName: string | null,
    receiverLastName: string | null,
    receiverImage: string | null,
    content: string,
    date: Date,
    status: number
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