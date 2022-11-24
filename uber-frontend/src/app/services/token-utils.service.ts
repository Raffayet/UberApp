import { Injectable } from '@angular/core';
import jwtDecode from "jwt-decode";

interface JwtToken{
  sub: string,
  iat: number,
  exp: number,
  role: string
}

@Injectable({
  providedIn: 'root'
})
export class TokenUtilsService {

  constructor() { }

  getUsernameFromToken(): string | null {
    try{
        let token: JwtToken = jwtDecode(localStorage.getItem("user") || "");
        return token.sub;
    }
    catch{
        return null;
    }
}

getRoleFromToken(): string | null{
    try{
        let token: JwtToken = jwtDecode(localStorage.getItem("user") || "");
        return token.role;
    }
    catch{
        return null;
    }
}

}
