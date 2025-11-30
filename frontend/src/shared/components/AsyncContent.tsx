import React from 'react';
import { Alert, Box, CircularProgress } from '@mui/material';

interface AsyncContentProps {
  loading: boolean;
  error: string | null;
  isEmpty: boolean;
  emptyMessage?: string;
  children: React.ReactNode;
}

export function AsyncContent({
  loading,
  error,
  isEmpty,
  emptyMessage = 'No data to display.',
  children,
}: AsyncContentProps) {
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

  if (isEmpty) {
    return (
      <Box mt={4}>
        <Alert severity="info">{emptyMessage}</Alert>
      </Box>
    );
  }

  return <>{children}</>;
}


