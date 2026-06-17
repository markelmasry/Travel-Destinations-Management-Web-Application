import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { WishlistItemResponseDto } from '../../shared/models/app.models';

@Injectable({
  providedIn: 'root',
})
export class WishlistService {
  private wishlistUrl = `${environment.apiUrl}/wishlist`;

  constructor(private http: HttpClient) {}

  addToWishlist(destinationId: number, username: string): Observable<WishlistItemResponseDto> {
    const params = new HttpParams()
      .set('destinationId', destinationId.toString())
      .set('username', username); // Sending username now!

    return this.http.post<WishlistItemResponseDto>(this.wishlistUrl, {}, { params });
  }

  getUserWishlist(username: string): Observable<WishlistItemResponseDto[]> {
    return this.http.get<WishlistItemResponseDto[]>(`${this.wishlistUrl}/user/${username}`);
  }

  removeFromWishlist(wishlistId: number, username: string): Observable<void> {
    return this.http.delete<void>(`${this.wishlistUrl}/${wishlistId}/user/${username}`);
  }
}
