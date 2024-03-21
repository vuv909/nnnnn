import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SignUp } from '../../types/signup.type';
import { Login } from '../../types/login.type';
import { Observable } from 'rxjs';
const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root',
})
export class PublicService {
  constructor(private http: HttpClient) {}

  signup(signupRequest: SignUp): Observable<any> {
    return this.http.post<[]>(BASIC_URL + 'api/auth/signup', signupRequest);
  }

  login(loginRequest: Login): Observable<any> {
    return this.http.post<[]>(BASIC_URL + 'api/auth/login', loginRequest);
  }

  changePasswordWhenLogin(data: any): Observable<any> {
    return this.http.post<[]>(
      BASIC_URL + 'api/auth/changePasswordWhenLogin',
      data
    );
  }

  changePassword(data: any): Observable<any> {
    return this.http.post<[]>(BASIC_URL + 'api/auth/changePassword', data);
  }
}
