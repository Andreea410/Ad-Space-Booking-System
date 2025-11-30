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

  const { deleteAdSpace, markAsBooked, updateAdSpace } = useAdSpacesStore((state) => ({
    deleteAdSpace: state.deleteAdSpace,
    markAsBooked: state.markAsBooked,
    updateAdSpace: state.updateAdSpace,
  }));

  const [editingSpace, setEditingSpace] = useState<AdSpace | null>(null);
  const [editingName, setEditingName] = useState('');
  const [deletingSpace, setDeletingSpace] = useState<AdSpace | null>(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);

  useEffect(() => {
    if (error) {
      setSnackbarOpen(true);
    }
  }, [error]);

  const handleCloseSnackbar = () => {
    setSnackbarOpen(false);
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
        onBookNow={(space) => markAsBooked(space.id)}
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

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={handleCloseSnackbar} severity="error" sx={{ width: '100%' }}>
          {error}
        </Alert>
      </Snackbar>
    </Box>
  );
}


