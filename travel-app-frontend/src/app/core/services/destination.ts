import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DestinationDto, Page, SuggestionsResponse } from '../../shared/models/app.models';

@Injectable({
  providedIn: 'root'
})
export class DestinationService {
  private destUrl = `${environment.apiUrl}/destinations`;

  constructor(private http: HttpClient) {}

  getAllDestinations(): Observable<DestinationDto[]> {
    return this.http.get<DestinationDto[]>(this.destUrl);
  }

  getDestinationById(id: number): Observable<DestinationDto> {
    return this.http.get<DestinationDto>(`${this.destUrl}/${id}`);
  }

  searchDestinations(country: string, page: number = 0, size: number = 10): Observable<Page<DestinationDto>> {
    const params = new HttpParams()
      .set('country', country)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<DestinationDto>>(`${this.destUrl}/search`, { params });
  }

  fetchSuggestions(country: string): Observable<SuggestionsResponse> {
    const params = new HttpParams().set('country', country);
    return this.http.get<SuggestionsResponse>(`${this.destUrl}/suggestions`, { params });
  }

  createDestination(destination: DestinationDto): Observable<DestinationDto> {
    return this.http.post<DestinationDto>(this.destUrl, destination);
  }

  createDestinationsBulk(destinations: DestinationDto[]): Observable<DestinationDto[]> {
    return this.http.post<DestinationDto[]>(`${this.destUrl}/bulk`, destinations);
  }

  deleteDestination(id: number): Observable<void> {
    return this.http.delete<void>(`${this.destUrl}/${id}`);
  }
}
