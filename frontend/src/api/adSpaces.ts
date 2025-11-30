import { getJson, postJson } from './httpClient';
import type { AdSpace, BookingRequest, CreateBookingRequestPayload } from './types';

export async function fetchAdSpaces(params?: { city?: string; type?: string }): Promise<AdSpace[]> {
  const search = new URLSearchParams();
  if (params?.city) search.set('city', params.city);
  if (params?.type) search.set('type', params.type);

  const query = search.toString();
  const path = `/api/v1/ad-spaces${query ? `?${query}` : ''}`;
  return getJson<AdSpace[]>(path);
}

export async function createBooking(
  payload: CreateBookingRequestPayload,
): Promise<BookingRequest> {
  return postJson<CreateBookingRequestPayload, BookingRequest>(
    '/api/v1/booking-requests',
    payload,
  );
}


