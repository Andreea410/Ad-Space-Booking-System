import React, { useState, useEffect } from 'react';
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from '@mui/material';
import { adSpaceEditSchema } from '../../../shared/validation/schemas';

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
  const [error, setError] = useState<string>('');

  useEffect(() => {
    if (!open) {
      setError('');
    }
  }, [open]);

  const handleNameChange = (value: string) => {
    onNameChange(value);
    // Clear error on change
    if (error) {
      setError('');
    }
  };

  const handleSave = () => {
    const result = adSpaceEditSchema.safeParse({ name });
    if (!result.success) {
      setError(result.error.issues[0].message);
      return;
    }
    onSave();
  };

  const handleKeyPress = (event: React.KeyboardEvent) => {
    if (event.key === 'Enter') {
      handleSave();
    }
  };

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
          onChange={(e) => handleNameChange(e.target.value)}
          onKeyPress={handleKeyPress}
          error={Boolean(error)}
          helperText={error || 'Enter a name for the ad space (3-100 characters)'}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onCancel}>Cancel</Button>
        <Button onClick={handleSave} variant="contained">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}
