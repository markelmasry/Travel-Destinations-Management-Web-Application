import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { WantVisitResponse } from '../../shared/models/app.models';

@Injectable({
  providedIn: 'root',
})
export class WantVisitService {
  private visitUrl = `${environment.apiUrl}/visit-list`;

  constructor(private http: HttpClient) {}

  addToVisitList(destinationId: number, userId: number): Observable<WantVisitResponse> {
    const params = new HttpParams()
      .set('destinationId', destinationId.toString())
      .set('userId', userId.toString());
    return this.http.post<WantVisitResponse>(this.visitUrl, {}, { params });
  }

  getUserVisitList(userId: number): Observable<WantVisitResponse[]> {
    return this.http.get<WantVisitResponse[]>(`${this.visitUrl}/user/${userId}`);
  }

  removeFromVisitList(visitId: number, userId: number): Observable<void> {
    return this.http.delete<void>(`${this.visitUrl}/${visitId}/user/${userId}`);
  }
}
