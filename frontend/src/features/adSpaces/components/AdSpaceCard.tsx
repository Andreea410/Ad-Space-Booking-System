import React from 'react';
import { Box, Button, Card, CardContent, Typography } from '@mui/material';
import type { AdSpace } from '../../../api/types';

interface AdSpaceCardProps {
  space: AdSpace;
}

export function AdSpaceCard({ space }: AdSpaceCardProps) {
  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          {space.name}
        </Typography>
        <Typography variant="body2" color="text.secondary" gutterBottom>
          {space.city} • {space.address}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Type: <strong>{space.type}</strong>
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Status: <strong>{space.status}</strong>
        </Typography>
        <Typography variant="subtitle1" sx={{ mt: 1.5 }}>
          {new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'EUR',
          }).format(space.pricePerDay)}{' '}
          / day
        </Typography>
        <Box mt={2}>
          <Button variant="contained" color="primary" fullWidth disabled>
            Request Booking (coming soon)
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
}


