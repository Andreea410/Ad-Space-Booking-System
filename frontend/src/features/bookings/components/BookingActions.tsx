import React from 'react';
import { Box, Button } from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import type { BookingRequest } from '../../../api/types';

interface BookingActionsProps {
  booking: BookingRequest;
  onApprove: (id: number) => void;
  onReject: (id: number) => void;
}

export function BookingActions({ booking, onApprove, onReject }: BookingActionsProps) {
  if (booking.status !== 'PENDING') {
    return null;
  }

  return (
    <Box sx={{ display: 'flex', gap: 1 }}>
      <Button
        variant="contained"
        color="success"
        size="small"
        startIcon={<CheckIcon />}
        onClick={() => onApprove(booking.id)}
      >
        Approve
      </Button>
      <Button
        variant="outlined"
        color="error"
        size="small"
        startIcon={<CloseIcon />}
        onClick={() => onReject(booking.id)}
      >
        Reject
      </Button>
    </Box>
  );
}

