import { getJson, patchJson } from './httpClient';
import type { BookingRequest, BookingStatus } from './types';

/**
 * Fetch all booking requests with optional status filter
 */
export async function fetchBookings(params?: {
  status?: BookingStatus;
}): Promise<BookingRequest[]> {
  const searchParams = new URLSearchParams();
  
  if (params?.status) {
    searchParams.append('status', params.status);
  }

  const url = `/api/v1/booking-requests${searchParams.toString() ? `?${searchParams.toString()}` : ''}`;
  return getJson<BookingRequest[]>(url);
}

/**
 * Approve a booking request
 */
export async function approveBooking(id: number): Promise<BookingRequest> {
  return patchJson<Record<string, never>, BookingRequest>(
    `/api/v1/booking-requests/${id}/approve`,
    {}
  );
}

/**
 * Reject a booking request
 */
export async function rejectBooking(id: number): Promise<BookingRequest> {
  return patchJson<Record<string, never>, BookingRequest>(
    `/api/v1/booking-requests/${id}/reject`,
    {}
  );
}

