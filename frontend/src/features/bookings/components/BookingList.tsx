import React from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Paper,
  Typography,
  Box,
} from '@mui/material';
import { SortableTableHead, type TableColumn } from '../../../shared/components/SortableTableHead';
import { StatusChip } from '../../../shared/components/StatusChip';
import { AsyncContent } from '../../../shared/components/AsyncContent';
import { BookingActions } from './BookingActions';
import { formatCurrencyEUR } from '../../../shared/utils/format';
import type { BookingRequest } from '../../../api/types';

interface BookingListProps {
  bookings: BookingRequest[];
  loading: boolean;
  error: string | null;
  onApprove: (id: number) => void;
  onReject: (id: number) => void;
}

const columns: TableColumn[] = [
  { id: 'id', label: 'ID', sortable: false, minWidth: 60 },
  { id: 'adSpaceName', label: 'Ad Space', sortable: false, minWidth: 150 },
  { id: 'advertiserName', label: 'Advertiser', sortable: false, minWidth: 150 },
  { id: 'advertiserEmail', label: 'Email', sortable: false, minWidth: 180 },
  { id: 'startDate', label: 'Start Date', sortable: false, minWidth: 110 },
  { id: 'endDate', label: 'End Date', sortable: false, minWidth: 110 },
  { id: 'totalCost', label: 'Total Cost', sortable: false, align: 'right', minWidth: 120 },
  { id: 'status', label: 'Status', sortable: false, minWidth: 100 },
  { id: 'actions', label: 'Actions', sortable: false, minWidth: 180 },
];

export function BookingList({
  bookings,
  loading,
  error,
  onApprove,
  onReject,
}: BookingListProps) {
  return (
    <AsyncContent
      loading={loading}
      error={error}
      isEmpty={bookings.length === 0}
      emptyMessage="No booking requests found"
    >
      <TableContainer component={Paper}>
        <Table>
          <SortableTableHead columns={columns} />
          <TableBody>
            {bookings.map((booking) => (
              <TableRow key={booking.id} hover>
                <TableCell>{booking.id}</TableCell>
                <TableCell>
                  <Typography variant="body2" fontWeight="medium">
                    Ad Space #{booking.adSpaceId}
                  </Typography>
                </TableCell>
                <TableCell>{booking.advertiserName}</TableCell>
                <TableCell>
                  <Typography variant="body2" color="text.secondary">
                    {booking.advertiserEmail}
                  </Typography>
                </TableCell>
                <TableCell>{formatDate(booking.startDate)}</TableCell>
                <TableCell>{formatDate(booking.endDate)}</TableCell>
                <TableCell align="right">
                  <Typography variant="body2" fontWeight="medium">
                    {formatCurrencyEUR(booking.totalCost)}
                  </Typography>
                </TableCell>
                <TableCell>
                  <StatusChip status={booking.status} />
                </TableCell>
                <TableCell>
                  <BookingActions
                    booking={booking}
                    onApprove={onApprove}
                    onReject={onReject}
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </AsyncContent>
  );
}

function formatDate(dateString: string): string {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-GB', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
}

