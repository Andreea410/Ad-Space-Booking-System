import React from 'react';
import { Card, CardContent, GridLegacy as Grid, Typography } from '@mui/material';
import type { AdSpace } from '../../../api/types';
import { formatCurrencyEUR } from '../../../shared/utils/format';

interface AdSpaceCardProps {
  space: AdSpace;
}

export function AdSpaceCard({ space }: AdSpaceCardProps) {
  return (
    <Card variant="outlined">
      <CardContent>
        <Grid container spacing={1}>
          <Grid item xs={12}>
            <Typography variant="h6" gutterBottom>
              {space.name}
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography variant="body2" color="text.secondary">
              {space.city} • {space.address}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography variant="body2" color="text.secondary">
              Type: <strong>{space.type}</strong>
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography variant="body2" color="text.secondary" align="right">
              Status: <strong>{space.status}</strong>
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography variant="subtitle1" sx={{ mt: 1 }}>
              {formatCurrencyEUR(space.pricePerDay)} / day
            </Typography>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
}

