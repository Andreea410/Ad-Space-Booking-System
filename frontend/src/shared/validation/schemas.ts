import { z } from 'zod';

export const adSpaceEditSchema = z.object({
  name: z
    .string()
    .min(3, 'Name must be at least 3 characters')
    .max(100, 'Name must not exceed 100 characters')
    .trim(),
});

export type AdSpaceEditInput = z.infer<typeof adSpaceEditSchema>;

export const bookingRequestSchema = z.object({
  advertiserName: z.string()
    .min(1, 'Advertiser name is required')
    .max(100, 'Advertiser name cannot exceed 100 characters')
    .trim(),
  advertiserEmail: z.string()
    .min(1, 'Email is required')
    .email('Please enter a valid email address')
    .trim(),
  startDate: z.date({
    message: 'Start date is required',
  }),
  endDate: z.date({
    message: 'End date is required',
  }),
}).refine(
  (data) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return data.startDate >= today;
  },
  {
    message: 'Start date must be today or in the future',
    path: ['startDate'],
  }
).refine(
  (data) => data.endDate > data.startDate,
  {
    message: 'End date must be after start date',
    path: ['endDate'],
  }
).refine(
  (data) => {
    const diffTime = Math.abs(data.endDate.getTime() - data.startDate.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays >= 7;
  },
  {
    message: 'Booking duration must be at least 7 days',
    path: ['endDate'],
  }
);

export type BookingRequestFormData = z.infer<typeof bookingRequestSchema>;

