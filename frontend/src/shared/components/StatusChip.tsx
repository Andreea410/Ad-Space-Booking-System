import React from 'react';
import { Chip } from '@mui/material';
import type { BookingStatus } from '../../api/types';

interface StatusChipProps {
  status: BookingStatus;
}

const statusConfig: Record<
  string,
  { label: string; color: 'default' | 'warning' | 'success' | 'error' }
> = {
  PENDING: { label: 'Pending', color: 'warning' },
  APPROVED: { label: 'Approved', color: 'success' },
  REJECTED: { label: 'Rejected', color: 'error' },
};

export function StatusChip({ status }: StatusChipProps) {
  const config = statusConfig[status] || { label: status, color: 'default' as const };
  
  return (
    <Chip
      label={config.label}
      color={config.color}
      size="small"
      sx={{ fontWeight: 'medium' }}
    />
  );
}

