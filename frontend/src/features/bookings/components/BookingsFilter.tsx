import React from 'react';
import {
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
} from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import type { BookingStatus } from '../../../api/types';

interface BookingsFilterProps {
  statusFilter: BookingStatus | null;
  statusOptions: Array<{ value: BookingStatus | null; label: string }>;
  loading: boolean;
  onStatusChange: (status: BookingStatus | null) => void;
  onRefresh: () => void;
}

export function BookingsFilter({
  statusFilter,
  statusOptions,
  loading,
  onStatusChange,
  onRefresh,
}: BookingsFilterProps) {
  return (
    <Box
      sx={{
        display: 'flex',
        gap: 2,
        mb: 3,
        flexWrap: 'wrap',
        alignItems: 'center',
      }}
    >
      <FormControl sx={{ minWidth: 200 }} size="small">
        <InputLabel>Status</InputLabel>
        <Select
          value={statusFilter || ''}
          label="Status"
          onChange={(e) => onStatusChange(e.target.value as BookingStatus || null)}
          disabled={loading}
        >
          {statusOptions.map((option) => (
            <MenuItem key={option.value || 'all'} value={option.value || ''}>
              {option.label}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <Button
        variant="outlined"
        startIcon={<RefreshIcon />}
        onClick={onRefresh}
        disabled={loading}
      >
        Refresh
      </Button>
    </Box>
  );
}

