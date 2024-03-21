import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private http: HttpClient) {}

  sendNotification(data: FormData): Observable<any> {
    return this.http.post(BASIC_URL + 'api/notify/createNotification', data);
  }

  viewNotificationByEmail(email: string): Observable<any> {
    return this.http.get(BASIC_URL + 'api/notify/view/' + email);
  }

  viewNotification(): Observable<any> {
    return this.http.get(BASIC_URL + 'api/notify/view');
  }

  totalNotiNotread(email: string): Observable<any> {
    return this.http.get(BASIC_URL + 'api/notify/viewTotal/' + email);
  }

  updateReadNoti(email: string): Observable<any> {
    return this.http.put(BASIC_URL + 'api/notify/read/' + email, {});
  }
}
