import React, { useState } from 'react';
import { Box } from '@mui/material';
import { useAdSpaces } from '../hooks/useAdSpaces';
import { AdSpacesFilters } from './AdSpacesFilters';
import { AdSpaceList } from './AdSpaceList';
import { PageHeader } from '../../../shared/components/PageHeader';
import { useAdSpacesStore } from '../store/adSpacesStore';
import type { AdSpace } from '../../../api/types';
import { EditAdSpaceDialog } from './EditAdSpaceDialog';

export function AdSpacesPage() {
  const {
    adSpaces,
    cityFilter,
    typeFilter,
    loading,
    error,
    availableCities,
    typeOptions,
    handleCityFilterChange,
    handleTypeFilterChange,
    handleRefresh,
  } = useAdSpaces();

  const { deleteAdSpace, markAsBooked, updateAdSpace } = useAdSpacesStore((state) => ({
    deleteAdSpace: state.deleteAdSpace,
    markAsBooked: state.markAsBooked,
    updateAdSpace: state.updateAdSpace,
  }));

  const [editingSpace, setEditingSpace] = useState<AdSpace | null>(null);
  const [editingName, setEditingName] = useState('');

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
        onBookNow={(space) => markAsBooked(space.id)}
        onEdit={handleOpenEdit}
        onDelete={(space) => deleteAdSpace(space.id)}
      />

      <EditAdSpaceDialog
        open={Boolean(editingSpace)}
        name={editingName}
        onNameChange={setEditingName}
        onCancel={handleCloseEdit}
        onSave={handleSaveEdit}
      />
    </Box>
  );
}


