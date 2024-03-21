import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

interface IAddFavourite {
  jobId: number;
  accountId: number;
}

const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root',
})
export class FavouriteService {
  constructor(private http: HttpClient) {}

  addFavouriteJob(data: IAddFavourite): Observable<any> {
    return this.http.post(BASIC_URL + 'api/favorite/favoritejob', data);
  }

  viewFavouriteJob(id: string, page?: number): Observable<any> {
    if (typeof page === 'number') {
      return this.http.get(
        BASIC_URL + 'api/favorite/favorite-jobs/' + id + '?page=' + page
      );
    }
    return this.http.get(BASIC_URL + 'api/favorite/favorite-jobs/' + id);
  }

  deleteFavoriteJob(id: number): Observable<any> {
    return this.http.delete(BASIC_URL + 'api/favorite/deletefavoritejob/' + id);
  }
}
