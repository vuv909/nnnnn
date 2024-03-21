import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  constructor(private http: HttpClient) {}

  viewProfileByEmail(data: any): Observable<any> {
    return this.http.post(BASIC_URL + 'api/profile/viewDetail', data);
  }

  findAccountByEmail(data: any): Observable<any> {
    return this.http.post(BASIC_URL + 'api/auth/findEmail', data);
  }

  updateProfileByEmail(data: any): Observable<any> {
    return this.http.put(BASIC_URL + 'api/profile/createprofile', data);
  }

  updateUsernameByEmail(data: any): Observable<any> {
    return this.http.put(BASIC_URL + 'api/auth/updateUsername', data);
  }

  updateCvByEmail(data: any): Observable<any> {
    return this.http.put(BASIC_URL + 'api/profile/update-profile-cv', data);
  }

  downloadCvById(id: string): Observable<any> {
    return this.http.get(BASIC_URL + `api/jobApply/downloadFile/${id}`);
  }


  downloadCvByInProfile(email: string): Observable<any> {
    return this.http.get(BASIC_URL + `api/profile/downloadFile/${email}`);
  }

  updateAvatar(data: any): Observable<any> {
    return this.http.put(BASIC_URL + 'api/profile/update-profile-avatar', data);
  }

  getAccountByEmail(email: string): Observable<any> {
    return this.http.get(BASIC_URL + 'api/auth/account/' + email);
  }
}
