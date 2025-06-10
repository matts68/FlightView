import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, Observable, of} from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class FormService {
  private serverUrl = 'http://localhost:8080/flights/api';

  constructor(private http: HttpClient) {}

  postRating(ratingData: any): Observable<any> {
    return this.http.post(this.serverUrl + '/view', ratingData);
  }
}
