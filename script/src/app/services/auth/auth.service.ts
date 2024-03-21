import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../storage/storage.service';

const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  sendOtpCode(data: any): Observable<any> {
    return this.http.post(BASIC_URL + 'api/auth/sendOtpCode', data);
  }

  verifyAccount(data: any): Observable<any> {
    return this.http.post(BASIC_URL + 'api/auth/verifyAccount', data);
  }

  getRole(): Observable<any> {
    return this.http.get(
      BASIC_URL + 'api/auth/role/' + StorageService.getEmail()
    );
  }
}
