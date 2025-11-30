import React, { useState, useEffect } from 'react';
import { Box, Snackbar, Alert } from '@mui/material';
import { useBookings } from '../hooks/useBookings';
import { BookingsFilter } from './BookingsFilter';
import { BookingList } from './BookingList';
import { PageHeader } from '../../../shared/components/PageHeader';
import { ConfirmDialog } from '../../../shared/components/ConfirmDialog';
import type { BookingRequest } from '../../../api/types';

export function BookingsPage() {
  const {
    bookings,
    loading,
    error,
    statusFilter,
    statusOptions,
    handleStatusFilterChange,
    handleRefresh,
    approveBooking,
    rejectBooking,
  } = useBookings();

  const [approvingBooking, setApprovingBooking] = useState<BookingRequest | null>(null);
  const [rejectingBooking, setRejectingBooking] = useState<BookingRequest | null>(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');
  const [snackbarSeverity, setSnackbarSeverity] = useState<'success' | 'error'>('error');

  useEffect(() => {
    if (error) {
      setSnackbarMessage(error);
      setSnackbarSeverity('error');
      setSnackbarOpen(true);
    }
  }, [error]);

  const handleCloseSnackbar = () => {
    setSnackbarOpen(false);
  };

  const showSuccessMessage = (message: string) => {
    setSnackbarMessage(message);
    setSnackbarSeverity('success');
    setSnackbarOpen(true);
  };

  const showErrorMessage = (message: string) => {
    setSnackbarMessage(message);
    setSnackbarSeverity('error');
    setSnackbarOpen(true);
  };

  const handleOpenApprove = (id: number) => {
    const booking = bookings.find((b) => b.id === id);
    if (booking) {
      setApprovingBooking(booking);
    }
  };

  const handleCloseApprove = () => {
    setApprovingBooking(null);
  };

  const handleConfirmApprove = async () => {
    if (!approvingBooking) return;
    
    try {
      await approveBooking(approvingBooking.id);
      showSuccessMessage(`Booking #${approvingBooking.id} approved successfully!`);
      handleCloseApprove();
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to approve booking';
      showErrorMessage(message);
      handleCloseApprove(); // Close the dialog even on error
    }
  };

  const handleOpenReject = (id: number) => {
    const booking = bookings.find((b) => b.id === id);
    if (booking) {
      setRejectingBooking(booking);
    }
  };

  const handleCloseReject = () => {
    setRejectingBooking(null);
  };

  const handleConfirmReject = async () => {
    if (!rejectingBooking) return;
    
    try {
      await rejectBooking(rejectingBooking.id);
      showSuccessMessage(`Booking #${rejectingBooking.id} rejected successfully!`);
      handleCloseReject();
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to reject booking';
      showErrorMessage(message);
      handleCloseReject(); // Close the dialog even on error
    }
  };

  return (
    <Box>
      <PageHeader
        title="Booking Requests"
        subtitle="Manage all booking requests for your ad spaces."
      />

      <BookingsFilter
        statusFilter={statusFilter}
        statusOptions={statusOptions}
        loading={loading}
        onStatusChange={handleStatusFilterChange}
        onRefresh={handleRefresh}
      />

      <BookingList
        bookings={bookings}
        loading={loading}
        error={error}
        onApprove={handleOpenApprove}
        onReject={handleOpenReject}
      />

      <ConfirmDialog
        open={Boolean(approvingBooking)}
        title="Approve Booking?"
        message={`Are you sure you want to approve booking #${approvingBooking?.id} from ${approvingBooking?.advertiserName}?`}
        confirmText="Approve"
        cancelText="Cancel"
        onConfirm={handleConfirmApprove}
        onCancel={handleCloseApprove}
      />

      <ConfirmDialog
        open={Boolean(rejectingBooking)}
        title="Reject Booking?"
        message={`Are you sure you want to reject booking #${rejectingBooking?.id} from ${rejectingBooking?.advertiserName}?`}
        confirmText="Reject"
        cancelText="Cancel"
        onConfirm={handleConfirmReject}
        onCancel={handleCloseReject}
      />

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={handleCloseSnackbar} severity={snackbarSeverity} sx={{ width: '100%' }}>
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Box>
  );
}

