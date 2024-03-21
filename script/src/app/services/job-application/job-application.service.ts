import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root',
})
export class JobApplicationService {
  constructor(private http: HttpClient) {}

  applyJobApplication(data: any): Observable<any> {
    return this.http.post(BASIC_URL + 'api/jobApply/createJobApply', data);
  }

  viewJobApplicationByEmail(email: string, page: number): Observable<any> {
    return this.http.get(
      BASIC_URL + 'api/jobApply/view/' + email + '?page=' + page
    );
  }

  viewJobApplicationByAdmin(page?: number): Observable<any> {
    if (typeof page !== 'number') {
      return this.http.get(BASIC_URL + 'api/jobApply/view');
    }
    return this.http.get(BASIC_URL + 'api/jobApply/view?page=' + page);
  }

  viewJobApplicationByHrEmail(
    email: string,
    status?: string | null,
    page?: number
  ): Observable<any> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    if (page) {
      params = params.set('page', page.toString());
    }
    return this.http.get(BASIC_URL + 'api/jobApply/view/hremail/' + email, {
      params,
    });
  }

  updateJobApplicationByHrEmail(data: any): Observable<any> {
    return this.http.put(BASIC_URL + 'api/jobApply/updateJobApply', data);
  }

  sendEmailInterview(data: any): Observable<any> {
    return this.http.post(BASIC_URL + 'api/jobApply/sendMailInterview', data);
  }
}
