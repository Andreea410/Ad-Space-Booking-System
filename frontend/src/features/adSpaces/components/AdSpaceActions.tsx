import React from 'react';
import { Button, Stack } from '@mui/material';

interface AdSpaceActionsProps {
  onBookNow: () => void;
  onEdit: () => void;
  onDelete: () => void;
}

export function AdSpaceActions({ onBookNow, onEdit, onDelete }: AdSpaceActionsProps) {
  return (
    <Stack direction="row" spacing={1} justifyContent="flex-end">
      <Button variant="contained" size="small" onClick={onBookNow}>
        Book Now
      </Button>
      <Button variant="outlined" size="small" onClick={onEdit}>
        Edit
      </Button>
      <Button variant="outlined" color="error" size="small" onClick={onDelete}>
        Delete
      </Button>
    </Stack>
  );
}


