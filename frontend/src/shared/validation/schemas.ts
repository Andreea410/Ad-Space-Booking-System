import { z } from 'zod';

export const adSpaceEditSchema = z.object({
  name: z
    .string()
    .min(3, 'Name must be at least 3 characters')
    .max(100, 'Name must not exceed 100 characters')
    .trim(),
});

export type AdSpaceEditInput = z.infer<typeof adSpaceEditSchema>;

