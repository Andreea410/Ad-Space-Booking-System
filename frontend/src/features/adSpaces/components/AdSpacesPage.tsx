import React from 'react';
import { Box, Typography } from '@mui/material';
import { useAdSpaces } from '../hooks/useAdSpaces';
import { AdSpacesFilters } from './AdSpacesFilters';
import { AdSpaceList } from './AdSpaceList';

export function AdSpacesPage() {
  const {
    adSpaces,
    cityFilter,
    loading,
    error,
    handleCityChange,
    handleSearch,
    handleRefresh,
  } = useAdSpaces();

  return (
    <Box>
      <Box mb={3}>
        <Typography variant="h4" gutterBottom>
          Available Ad Spaces
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Search for available ad spaces and get ready to place your booking request.
        </Typography>
      </Box>

      <AdSpacesFilters
        cityFilter={cityFilter}
        loading={loading}
        onCityChange={handleCityChange}
        onSearch={handleSearch}
        onRefresh={handleRefresh}
      />

      <AdSpaceList adSpaces={adSpaces} loading={loading} error={error} />
    </Box>
  );
}


