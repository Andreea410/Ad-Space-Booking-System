import React from 'react';
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from '@mui/material';

interface EditAdSpaceDialogProps {
  open: boolean;
  name: string;
  onNameChange: (value: string) => void;
  onCancel: () => void;
  onSave: () => void;
}

export function EditAdSpaceDialog({
  open,
  name,
  onNameChange,
  onCancel,
  onSave,
}: EditAdSpaceDialogProps) {
  return (
    <Dialog open={open} onClose={onCancel} maxWidth="sm" fullWidth>
      <DialogTitle>Edit ad space</DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          label="Name"
          fullWidth
          value={name}
          onChange={(e) => onNameChange(e.target.value)}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onCancel}>Cancel</Button>
        <Button onClick={onSave} variant="contained">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}
