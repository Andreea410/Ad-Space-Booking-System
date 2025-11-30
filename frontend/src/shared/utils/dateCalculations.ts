export function calculateDaysBetween(startDate: Date, endDate: Date): number {
  const diffTime = Math.abs(endDate.getTime() - startDate.getTime());
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays;
}

export function calculateTotalCost(
  startDate: Date,
  endDate: Date,
  pricePerDay: number,
): number {
  const days = calculateDaysBetween(startDate, endDate);
  return days * pricePerDay;
}

export function formatDateForAPI(date: Date): string {
  return date.toISOString().split('T')[0];
}
