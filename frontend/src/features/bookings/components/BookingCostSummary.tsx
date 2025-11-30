import React from 'react';
import { Box, Typography } from '@mui/material';
import { formatCurrencyEUR } from '../../../shared/utils/format';

interface BookingCostSummaryProps {
  totalCost: number;
  days: number;
  pricePerDay: number;
}

export function BookingCostSummary({ totalCost, days, pricePerDay }: BookingCostSummaryProps) {
  if (totalCost <= 0) return null;

  return (
    <Box mt={3} p={2} bgcolor="primary.50" borderRadius={1}>
      <Typography variant="body2" color="text.secondary">
        Total Cost
      </Typography>
      <Typography variant="h5" color="primary" fontWeight="bold">
        {formatCurrencyEUR(totalCost)}
      </Typography>
      <Typography variant="caption" color="text.secondary">
        {days} days × {formatCurrencyEUR(pricePerDay)}
      </Typography>
    </Box>
  );
}

