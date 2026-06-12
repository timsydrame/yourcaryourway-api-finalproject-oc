import { OfferResponse } from './offer.interface';

export interface ReservationRequest {
  offerId: string;
  departureDateTime: string;
  returnDateTime: string;
}

export interface ReservationResponse {
  id: string;
  offer: OfferResponse;
  departureDateTime: string;
  returnDateTime: string;
  totalPrice: number;
  status: string;
  createdAt: string;
}
