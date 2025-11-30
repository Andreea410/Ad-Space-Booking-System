import React from 'react';
import { Box, Typography } from '@mui/material';
import type { AdSpace } from '../../../api/types';
import { formatCurrencyEUR } from '../../../shared/utils/format';

interface AdSpaceSummaryCardProps {
  adSpace: AdSpace;
}

export function AdSpaceSummaryCard({ adSpace }: AdSpaceSummaryCardProps) {
  return (
    <Box mb={3} p={2} bgcolor="grey.50" borderRadius={1}>
      <Typography variant="h6" gutterBottom>
        {adSpace.name}
      </Typography>
      <Typography variant="body2" color="text.secondary">
        {adSpace.type} • {adSpace.city}
      </Typography>
      <Typography variant="body2" color="text.secondary">
        {adSpace.address}
      </Typography>
      <Typography variant="body1" fontWeight="medium" mt={1}>
        {formatCurrencyEUR(adSpace.pricePerDay)} / day
      </Typography>
    </Box>
  );
}

