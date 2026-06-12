import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ReservationRequest,
  ReservationResponse,
} from '../interfaces/reservation.interface';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/v1/reservations`;

  create(request: ReservationRequest): Observable<ReservationResponse> {
    return this.http.post<ReservationResponse>(this.apiUrl, request);
  }

  getMyReservations(): Observable<ReservationResponse[]> {
    return this.http.get<ReservationResponse[]>(`${this.apiUrl}/me`);
  }

  cancel(id: string): Observable<ReservationResponse> {
    return this.http.patch<ReservationResponse>(
      `${this.apiUrl}/${id}/cancel`,
      {},
    );
  }
}
