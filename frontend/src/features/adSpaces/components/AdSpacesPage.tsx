import React, { useState, useEffect } from 'react';
import { Box, Snackbar, Alert } from '@mui/material';
import { useAdSpaces } from '../hooks/useAdSpaces';
import { AdSpacesFilters } from './AdSpacesFilters';
import { AdSpaceList } from './AdSpaceList';
import { PageHeader } from '../../../shared/components/PageHeader';
import { ConfirmDialog } from '../../../shared/components/ConfirmDialog';
import { useAdSpacesStore } from '../store/adSpacesStore';
import type { AdSpace } from '../../../api/types';
import { EditAdSpaceDialog } from './EditAdSpaceDialog';
import { BookingRequestForm } from '../../bookings/components/BookingRequestForm';
import { createBooking } from '../../../api/adSpaces';

export function AdSpacesPage() {
  const {
    adSpaces,
    cityFilter,
    typeFilter,
    sortBy,
    sortOrder,
    loading,
    error,
    availableCities,
    typeOptions,
    handleCityFilterChange,
    handleTypeFilterChange,
    handleRefresh,
    handleSort,
  } = useAdSpaces();

  const { deleteAdSpace, updateAdSpace } = useAdSpacesStore((state) => ({
    deleteAdSpace: state.deleteAdSpace,
    updateAdSpace: state.updateAdSpace,
  }));

  const [editingSpace, setEditingSpace] = useState<AdSpace | null>(null);
  const [editingName, setEditingName] = useState('');
  const [deletingSpace, setDeletingSpace] = useState<AdSpace | null>(null);
  const [bookingSpace, setBookingSpace] = useState<AdSpace | null>(null);
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

  const handleOpenEdit = (space: AdSpace) => {
    setEditingSpace(space);
    setEditingName(space.name);
  };

  const handleCloseEdit = () => {
    setEditingSpace(null);
    setEditingName('');
  };

  const handleSaveEdit = () => {
    if (!editingSpace) return;
    const trimmed = editingName.trim();
    if (!trimmed || trimmed === editingSpace.name) {
      handleCloseEdit();
      return;
    }
    updateAdSpace(editingSpace.id, { name: trimmed });
    handleCloseEdit();
  };

  const handleOpenDelete = (space: AdSpace) => {
    setDeletingSpace(space);
  };

  const handleCloseDelete = () => {
    setDeletingSpace(null);
  };

  const handleConfirmDelete = () => {
    if (!deletingSpace) return;
    deleteAdSpace(deletingSpace.id);
    handleCloseDelete();
  };

  const handleOpenBooking = (space: AdSpace) => {
    setBookingSpace(space);
  };

  const handleCloseBooking = () => {
    setBookingSpace(null);
  };

  const handleSubmitBooking = async (data: {
    adSpaceId: number;
    advertiserName: string;
    advertiserEmail: string;
    startDate: string;
    endDate: string;
  }) => {
    try {
      await createBooking(data);
      showSuccessMessage('Booking request submitted successfully!');
      handleRefresh();
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to submit booking request';
      showErrorMessage(message);
      throw err;
    }
  };

  return (
    <Box>
      <PageHeader
        title="Available Ad Spaces"
        subtitle="Search for available ad spaces and get ready to place your booking request."
      />

      <AdSpacesFilters
        cityFilter={cityFilter}
        typeFilter={typeFilter}
        cityOptions={availableCities}
        typeOptions={typeOptions}
        loading={loading}
        onCityChange={handleCityFilterChange}
        onTypeChange={handleTypeFilterChange}
        onRefresh={handleRefresh}
      />

      <AdSpaceList
        adSpaces={adSpaces}
        loading={loading}
        error={error}
        sortBy={sortBy}
        sortOrder={sortOrder}
        onSort={handleSort}
        onBookNow={handleOpenBooking}
        onEdit={handleOpenEdit}
        onDelete={handleOpenDelete}
      />

      <EditAdSpaceDialog
        open={Boolean(editingSpace)}
        name={editingName}
        onNameChange={setEditingName}
        onCancel={handleCloseEdit}
        onSave={handleSaveEdit}
      />

      <ConfirmDialog
        open={Boolean(deletingSpace)}
        title="Delete Ad Space?"
        message={`Are you sure you want to delete "${deletingSpace?.name}"? This action cannot be undone.`}
        confirmText="Delete"
        cancelText="Cancel"
        onConfirm={handleConfirmDelete}
        onCancel={handleCloseDelete}
      />

      <BookingRequestForm
        open={Boolean(bookingSpace)}
        adSpace={bookingSpace}
        onClose={handleCloseBooking}
        onSubmit={handleSubmitBooking}
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


