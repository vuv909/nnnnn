import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root'
})
export class JobCategoryService {

  constructor(private http: HttpClient) {}

  getAllBranch(): Observable<any>{
    return this.http.get(BASIC_URL + 'api/job/viewBranch');
  }

  addJobCategory(data : any): Observable<any>{
    return this.http.post(BASIC_URL + 'api/job/job_category', data);
  }

  updateJobCategory(data : any): Observable<any>{
    return this.http.post(BASIC_URL + 'api/job/update/job_category', data);
  }

  viewJobCategory(page ?: number): Observable<any>{
    if(typeof page === 'number'){
      return this.http.get(BASIC_URL + 'api/job/viewJobCategory?page=' + page);
    }
    return this.http.get(BASIC_URL + 'api/job/viewJobCategory');
  }

  deleteJobCategoryById(id : number): Observable<any>{
    return this.http.delete(BASIC_URL + 'api/job/delete/job_category/'+id);
  }

}
