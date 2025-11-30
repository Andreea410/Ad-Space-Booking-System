import React from 'react';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { Dayjs } from 'dayjs';

interface FormDatePickerProps {
  label: string;
  value: Dayjs | null;
  onChange: (value: Dayjs | null) => void;
  disabled?: boolean;
  minDate?: Dayjs;
  error?: string;
  helperText?: string;
}

export function FormDatePicker({ 
  label,
  value,
  onChange,
  disabled,
  minDate,
  error, 
  helperText 
}: FormDatePickerProps) {
  return (
    <DatePicker
      label={label}
      value={value}
      onChange={onChange}
      disabled={disabled}
      minDate={minDate}
      format="DD/MM/YYYY"
      slotProps={{
        textField: {
          fullWidth: true,
          margin: 'normal',
          required: true,
          error: Boolean(error),
          helperText: error || helperText || ' ',
          variant: 'outlined',
          placeholder: 'DD/MM/YYYY',
        },
        field: {
          clearable: true,
        },
      }}
    />
  );
}
