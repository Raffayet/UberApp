import { DriverRegister } from './../../../model/DriverRegister';


const goodDriverRegisterData:DriverRegister = {
    firstName: 'Pera',
    lastName: 'Peric',
    email: 'pera@gmail.com',
    password: 'Pera1234',
    confirmPassword: 'Pera1234',
    city: 'Novi Sad',
    telephone: '0605554444',
    vehicle: {
        model:"Skoda",
        vehicleType:"Standard"
    }
}

const badDriverRegisterData:DriverRegister = {
    firstName: 'Pera',
    lastName: 'Peric',
    email: '',
    password: 'Pera1234',
    confirmPassword: 'Pera1234',
    city: 'Novi Sad',
    telephone: '0605554444',
    vehicle: {
        model:"Skoda",
        vehicleType:"Standard"
    }
}

export {goodDriverRegisterData, badDriverRegisterData};