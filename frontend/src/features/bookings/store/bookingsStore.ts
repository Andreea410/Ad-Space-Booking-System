import { create } from 'zustand';
import type { StateCreator } from 'zustand';
import { fetchBookings, approveBooking as apiApproveBooking, rejectBooking as apiRejectBooking } from '../../../api/bookings';
import type { BookingRequest, BookingStatus } from '../../../api/types';

interface BookingsState {
  bookings: BookingRequest[];
  loading: boolean;
  error: string | null;
  statusFilter: BookingStatus | null;
  
  loadBookings: () => Promise<void>;
  setStatusFilter: (status: BookingStatus | null) => void;
  approveBooking: (id: number) => Promise<void>;
  rejectBooking: (id: number) => Promise<void>;
}

export const useBookingsStore = create<BookingsState>(
  ((set, get) => ({
    bookings: [],
    loading: false,
    error: null,
    statusFilter: null,

    loadBookings: async () => {
      set({ loading: true, error: null });
      try {
        const { statusFilter } = get();
        const bookings = await fetchBookings(
          statusFilter ? { status: statusFilter } : undefined
        );
        set({ bookings, loading: false });
      } catch (err) {
        set({
          error: err instanceof Error ? err.message : 'Failed to load bookings',
          loading: false,
        });
      }
    },

    setStatusFilter: (status) => {
      set({ statusFilter: status });
      get().loadBookings();
    },

    approveBooking: async (id) => {
      set({ loading: true, error: null });
      try {
        const updatedBooking = await apiApproveBooking(id);
        set((state) => ({
          bookings: state.bookings.map((booking) =>
            booking.id === id ? updatedBooking : booking
          ),
          loading: false,
        }));
      } catch (err) {
        set({
          error: err instanceof Error ? err.message : 'Failed to approve booking',
          loading: false,
        });
        throw err;
      }
    },

    rejectBooking: async (id) => {
      set({ loading: true, error: null });
      try {
        const updatedBooking = await apiRejectBooking(id);
        set((state) => ({
          bookings: state.bookings.map((booking) =>
            booking.id === id ? updatedBooking : booking
          ),
          loading: false,
        }));
      } catch (err) {
        set({
          error: err instanceof Error ? err.message : 'Failed to reject booking',
          loading: false,
        });
        throw err;
      }
    },
  })) as StateCreator<BookingsState>
);

