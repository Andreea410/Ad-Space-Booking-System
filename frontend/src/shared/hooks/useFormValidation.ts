import { useState, useCallback } from 'react';
import { ZodError, ZodSchema } from 'zod';

interface UseFormValidationOptions<T> {
  schema: ZodSchema<T>;
  onSubmit: (data: T) => Promise<void>;
}

export function useFormValidation<T>({ schema, onSubmit }: UseFormValidationOptions<T>) {
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const clearErrors = useCallback(() => {
    setErrors({});
    setSubmitError(null);
  }, []);

  const validateAndSubmit = useCallback(async (data: unknown) => {
    setErrors({});
    setSubmitError(null);

    try {
      const validatedData = schema.parse(data);
      setSubmitting(true);
      await onSubmit(validatedData);
    } catch (error) {
      if (error instanceof ZodError) {
        // Handle validation errors - display them inline
        const fieldErrors: Record<string, string> = {};
        error.issues.forEach((issue) => {
          const field = issue.path[0];
          if (field) {
            fieldErrors[String(field)] = issue.message;
          }
        });
        setErrors(fieldErrors);
        // Don't re-throw validation errors - they're handled inline
      } else {
        // Handle API/submission errors - display them as alerts
        setSubmitError(
          error instanceof Error ? error.message : 'An unexpected error occurred'
        );
        // Re-throw API errors so parent can handle them if needed
        throw error;
      }
    } finally {
      setSubmitting(false);
    }
  }, [schema, onSubmit]);

  return {
    errors,
    submitting,
    submitError,
    validateAndSubmit,
    clearErrors,
  };
}

