import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  OfferResponse,
  OfferSearchRequest,
} from '../interfaces/offer.interface';

@Injectable({
  providedIn: 'root',
})
export class OfferService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/v1/offers`;

  searchOffers(request: OfferSearchRequest): Observable<OfferResponse[]> {
    return this.http.post<OfferResponse[]>(`${this.apiUrl}/search`, request);
  }

  findById(id: string): Observable<OfferResponse> {
    return this.http.get<OfferResponse>(`${this.apiUrl}/${id}`);
  }
}
