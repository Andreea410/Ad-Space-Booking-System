import React from 'react';
import { Dayjs } from 'dayjs';
import dayjs from 'dayjs';
import { FormTextField } from '../../../shared/components/FormTextField';
import { FormDatePicker } from '../../../shared/components/FormDatePicker';

interface BookingFormFieldsProps {
  advertiserName: string;
  advertiserEmail: string;
  startDate: Dayjs | null;
  endDate: Dayjs | null;
  errors: {
    advertiserName?: string;
    advertiserEmail?: string;
    startDate?: string;
    endDate?: string;
  };
  disabled: boolean;
  onAdvertiserNameChange: (value: string) => void;
  onAdvertiserEmailChange: (value: string) => void;
  onStartDateChange: (value: Dayjs | null) => void;
  onEndDateChange: (value: Dayjs | null) => void;
}

export function BookingFormFields({
  advertiserName,
  advertiserEmail,
  startDate,
  endDate,
  errors,
  disabled,
  onAdvertiserNameChange,
  onAdvertiserEmailChange,
  onStartDateChange,
  onEndDateChange,
}: BookingFormFieldsProps) {
  return (
    <>
      <FormTextField
        label="Advertiser Name"
        value={advertiserName}
        onChange={(e) => onAdvertiserNameChange(e.target.value)}
        error={errors.advertiserName}
        helperText={errors.advertiserName || 'Enter your company or personal name'}
        disabled={disabled}
        required
        autoFocus
      />

      <FormTextField
        label="Advertiser Email"
        type="email"
        value={advertiserEmail}
        onChange={(e) => onAdvertiserEmailChange(e.target.value)}
        error={errors.advertiserEmail}
        helperText={errors.advertiserEmail || 'We will send confirmation to this email'}
        disabled={disabled}
        required
      />

      <FormDatePicker
        label="Start Date"
        value={startDate}
        onChange={onStartDateChange}
        disabled={disabled}
        minDate={dayjs()}
        error={errors.startDate}
        helperText={errors.startDate || 'Select start date (today or later)'}
      />

      <FormDatePicker
        label="End Date"
        value={endDate}
        onChange={onEndDateChange}
        disabled={disabled}
        minDate={startDate || dayjs()}
        error={errors.endDate}
        helperText={errors.endDate || 'Select end date (minimum 7 days from start)'}
      />
    </>
  );
}

