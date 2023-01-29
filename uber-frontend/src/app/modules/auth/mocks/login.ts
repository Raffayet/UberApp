import { FormControl, FormGroup, Validators } from '@angular/forms';

const loginForm = new FormGroup({
    'email': new FormControl('', Validators.required),
    'password': new FormControl('', Validators.required)
});

const filledLoginForm = new FormGroup({
    'email': new FormControl('sasalukic@gmail.com', Validators.required),
    'password': new FormControl('sasa123', Validators.required)
});

const tokenResponse = {
    accessToken: 
    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXNhbHVraWNAZ21haWwuY29tIiwicm9sZSI6IkNMSUVOVCIsImV4cCI6MTY3NDYyMjAyOCwiaWF0IjoxNjc0NTUyMDI4LCJ1c2VyRHRvIjp7Im5hbWUiOiJTYXNhIiwic3VybmFtZSI6Ikx1a2ljIiwiZW1haWwiOiJzYXNhbHVraWNAZ21haWwuY29tIiwicm9sZSI6IkNMSUVOVCIsImNpdHkiOiJOb3ZpIFNhZCIsInBob25lTnVtYmVyIjoiMDYxMzQ1ODkwIiwiYWN0aXZlQWNjb3VudCI6dHJ1ZSwiYmxvY2tlZCI6ZmFsc2UsImRyaXZpbmdTdGF0dXMiOiJPTkxJTkUiLCJhY2NvdW50U3RhdHVzIjoiQUNUSVZFIn19.au2ebu4OmI22tfPBAR9Jiy-M1OsMJnxBFsk6z1oRG8Y_dt8DTDHux6SezVsfF3_ltUOdhALVZ5I5aBcOsU7b8w",
    message: "OK",
    tokenType: "Bearer "
};

export { loginForm, tokenResponse, filledLoginForm };