import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Alert,
  CircularProgress,
} from '@mui/material';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { Dayjs } from 'dayjs';
import type { AdSpace } from '../../../api/types';
import { bookingRequestSchema } from '../../../shared/validation/schemas';
import { calculateTotalCost, calculateDaysBetween, formatDateForAPI } from '../../../shared/utils/dateCalculations';
import { useFormValidation } from '../../../shared/hooks/useFormValidation';
import { AdSpaceSummaryCard } from './AdSpaceSummaryCard';
import { BookingFormFields } from './BookingFormFields';
import { BookingCostSummary } from './BookingCostSummary';

interface BookingRequestFormProps {
  open: boolean;
  adSpace: AdSpace | null;
  onClose: () => void;
  onSubmit: (data: {
    adSpaceId: number;
    advertiserName: string;
    advertiserEmail: string;
    startDate: string;
    endDate: string;
  }) => Promise<void>;
}

export function BookingRequestForm({
  open,
  adSpace,
  onClose,
  onSubmit,
}: BookingRequestFormProps) {
  const [advertiserName, setAdvertiserName] = useState('');
  const [advertiserEmail, setAdvertiserEmail] = useState('');
  const [startDate, setStartDate] = useState<Dayjs | null>(null);
  const [endDate, setEndDate] = useState<Dayjs | null>(null);

  const { errors, submitting, submitError, validateAndSubmit, clearErrors } = useFormValidation({
    schema: bookingRequestSchema,
    onSubmit: async (validatedData) => {
      if (!adSpace) return;
      
      await onSubmit({
        adSpaceId: adSpace.id,
        advertiserName: validatedData.advertiserName,
        advertiserEmail: validatedData.advertiserEmail,
        startDate: formatDateForAPI(validatedData.startDate),
        endDate: formatDateForAPI(validatedData.endDate),
      });
      
      onClose();
    },
  });

  // Reset form when dialog opens
  useEffect(() => {
    if (open) {
      setAdvertiserName('');
      setAdvertiserEmail('');
      setStartDate(null);
      setEndDate(null);
      clearErrors();
    }
  }, [open, clearErrors]);

  const totalCost =
    adSpace && startDate && endDate
      ? calculateTotalCost(startDate.toDate(), endDate.toDate(), adSpace.pricePerDay)
      : 0;

  const days =
    startDate && endDate ? calculateDaysBetween(startDate.toDate(), endDate.toDate()) : 0;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      await validateAndSubmit({
        advertiserName,
        advertiserEmail,
        startDate: startDate?.toDate(),
        endDate: endDate?.toDate(),
      });
    } catch (error) {
    }
  };

  if (!adSpace) return null;

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
        <form onSubmit={handleSubmit}>
          <DialogTitle>Book Ad Space</DialogTitle>
          
          <DialogContent>
            <AdSpaceSummaryCard adSpace={adSpace} />

            {submitError && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {submitError}
              </Alert>
            )}

            <BookingFormFields
              advertiserName={advertiserName}
              advertiserEmail={advertiserEmail}
              startDate={startDate}
              endDate={endDate}
              errors={errors}
              disabled={submitting}
              onAdvertiserNameChange={setAdvertiserName}
              onAdvertiserEmailChange={setAdvertiserEmail}
              onStartDateChange={setStartDate}
              onEndDateChange={setEndDate}
            />

            <BookingCostSummary
              totalCost={totalCost}
              days={days}
              pricePerDay={adSpace.pricePerDay}
            />
          </DialogContent>

          <DialogActions>
            <Button onClick={onClose} disabled={submitting}>
              Cancel
            </Button>
            <Button
              type="submit"
              variant="contained"
              disabled={submitting}
              startIcon={submitting ? <CircularProgress size={20} /> : null}
            >
              {submitting ? 'Submitting...' : 'Submit Booking Request'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </LocalizationProvider>
  );
}

