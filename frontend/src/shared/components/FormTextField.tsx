import React from 'react';
import { TextField, TextFieldProps } from '@mui/material';

interface FormTextFieldProps extends Omit<TextFieldProps, 'error' | 'helperText'> {
  error?: string;
  helperText?: string;
}

export function FormTextField({ error, helperText, value, onChange, ...props }: FormTextFieldProps) {
  return (
    <TextField
      {...props}
      value={value}
      onChange={onChange}
      error={Boolean(error)}
      helperText={error || helperText || ' '}
      fullWidth
      margin="normal"
      variant="outlined"
    />
  );
}
