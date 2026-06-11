export interface AgencyResponse {
  id: string;
  name: string;
  city: string;
  country: string;
  address: string;
  phone: string | null;
}

export interface VehicleResponse {
  id: string;
  brand: string;
  model: string;
  year: number;
  acrissCode: string;
  color: string;
  fuelType: string;
  transmission: string;
  seats: number;
  photoUrl: string | null;
}

export interface OfferResponse {
  id: string;
  vehicle: VehicleResponse;
  agencyDeparture: AgencyResponse;
  agencyReturn: AgencyResponse;
  pricePerDay: number;
  available: boolean;
}

export interface OfferSearchRequest {
  departureCity: string;
  returnCity: string;
  departureDateTime: string;
  returnDateTime: string;
  acrissCode?: string;
}
