import { useEffect, useCallback } from 'react';
import { useBookingsStore } from '../store/bookingsStore';
import type { BookingStatus } from '../../../api/types';

export function useBookings() {
  const {
    bookings,
    loading,
    error,
    statusFilter,
    loadBookings,
    setStatusFilter,
    approveBooking,
    rejectBooking,
  } = useBookingsStore((state) => ({
    bookings: state.bookings,
    loading: state.loading,
    error: state.error,
    statusFilter: state.statusFilter,
    loadBookings: state.loadBookings,
    setStatusFilter: state.setStatusFilter,
    approveBooking: state.approveBooking,
    rejectBooking: state.rejectBooking,
  }));

  // Load bookings on mount
  useEffect(() => {
    loadBookings();
  }, [loadBookings]);

  const handleStatusFilterChange = useCallback(
    (status: BookingStatus | null) => {
      setStatusFilter(status);
    },
    [setStatusFilter]
  );

  const handleRefresh = useCallback(() => {
    loadBookings();
  }, [loadBookings]);

  const statusOptions: Array<{ value: BookingStatus | null; label: string }> = [
    { value: null, label: 'All Statuses' },
    { value: 'PENDING', label: 'Pending' },
    { value: 'APPROVED', label: 'Approved' },
    { value: 'REJECTED', label: 'Rejected' },
  ];

  return {
    bookings,
    loading,
    error,
    statusFilter,
    statusOptions,
    handleStatusFilterChange,
    handleRefresh,
    approveBooking,
    rejectBooking,
  };
}

