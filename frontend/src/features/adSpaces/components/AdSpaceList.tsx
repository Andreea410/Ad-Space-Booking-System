import React from 'react';
import {
  Alert,
  Box,
  CircularProgress,
  GridLegacy as Grid,
} from '@mui/material';
import type { AdSpace } from '../../../api/types';
import { AdSpaceCard } from './AdSpaceCard';

interface AdSpaceListProps {
  adSpaces: AdSpace[];
  loading: boolean;
  error: string | null;
}

export function AdSpaceList({ adSpaces, loading, error }: AdSpaceListProps) {
  if (loading) {
    return (
      <Box display="flex" justifyContent="center" my={4}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box mb={3}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  if (adSpaces.length === 0) {
    return (
      <Box mt={4}>
        <Alert severity="info">No ad spaces found. Try adjusting your filters.</Alert>
      </Box>
    );
  }

  return (
    <Grid container spacing={3}>
      {adSpaces.map((space) => (
        <Grid item xs={12} sm={6} md={4} key={space.id}>
          <AdSpaceCard space={space} />
        </Grid>
      ))}
    </Grid>
  );
}


