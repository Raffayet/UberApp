import { Register } from "src/app/model/Register";

const goodUserRegisterData: Register = {
    firstName: 'Pera',
    lastName: 'Peric',
    email: 'pera@gmail.com',
    password: 'Pera1234',
    confirmPassword: 'Pera1234',
    city: 'Novi Sad',
    telephone: '0605554444',
    provider: 'LOCAL'
}

const badUserRegisterData: Register = {
    firstName: 'Pera',
    lastName: 'Peric',
    email: '',
    password: 'Pera1234',
    confirmPassword: 'Pera1234',
    city: 'Novi Sad',
    telephone: '0605554444',
    provider: 'LOCAL'
}

export {goodUserRegisterData, badUserRegisterData};