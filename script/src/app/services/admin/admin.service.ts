import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../storage/storage.service';

const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private http: HttpClient) {}

  getAccountByEnabledStatus(data: any, page?: number): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${StorageService.getToken()}`,
    });

    let params = new HttpParams(); // Initialize params object

    if (page) {
      params = params.set('page', page.toString()); // Set 'page' parameter if provided
    }

    // Make the HTTP POST request with parameters
    return this.http.post(BASIC_URL + 'api/admin/accounts', data, {
      headers: headers,
      params: params, // Include params in the request options
    });
  }

  blockAndActiveAccount(data: any) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${StorageService.getToken()}`,
    });
    return this.http.post(BASIC_URL + 'api/admin/blockAccount', data, {
      headers: headers,
    });
  }

  createAccountHR(data: any) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${StorageService.getToken()}`,
    });
    return this.http.post(BASIC_URL + 'api/admin/createAccount', data, {
      headers: headers,
    });
  }
}
