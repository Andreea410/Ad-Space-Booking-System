import { getJson, postJson } from './httpClient';
import type { AdSpace, BookingRequest, CreateBookingRequestPayload } from './types';

export async function fetchAdSpaces(params?: {
  city?: string;
  type?: string;
  sortBy?: string;
  sortOrder?: string;
}): Promise<AdSpace[]> {
  const search = new URLSearchParams();
  if (params?.city) search.set('city', params.city);
  if (params?.type) search.set('type', params.type);
  if (params?.sortBy) search.set('sortBy', params.sortBy);
  if (params?.sortOrder) search.set('sortOrder', params.sortOrder);

  const query = search.toString();
  const path = `/api/v1/ad-spaces${query ? `?${query}` : ''}`;
  return getJson<AdSpace[]>(path);
}

export async function updateAdSpace(
  id: number,
  updates: { name: string },
): Promise<AdSpace> {
  const response = await fetch(`/api/v1/ad-spaces/${id}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(updates),
  });
  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    const message = errorData?.message || `Failed to update ad space: ${response.statusText}`;
    throw new Error(message);
  }
  return response.json();
}

export async function deleteAdSpace(id: number): Promise<void> {
  const response = await fetch(`/api/v1/ad-spaces/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    const message = errorData?.message || `Failed to delete ad space: ${response.statusText}`;
    throw new Error(message);
  }
}

export async function bookAdSpace(id: number): Promise<AdSpace> {
  const response = await fetch(`/api/v1/ad-spaces/${id}/book`, {
    method: 'PATCH',
  });
  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    const message = errorData?.message || `Failed to book ad space: ${response.statusText}`;
    throw new Error(message);
  }
  return response.json();
}

export async function createBooking(
  payload: CreateBookingRequestPayload,
): Promise<BookingRequest> {
  return postJson<CreateBookingRequestPayload, BookingRequest>(
    '/api/v1/booking-requests',
    payload,
  );
}


