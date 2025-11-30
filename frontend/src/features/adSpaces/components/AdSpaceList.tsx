import React from 'react';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TableSortLabel,
  Typography,
} from '@mui/material';
import type { AdSpace } from '../../../api/types';
import type { SortField, SortOrder } from '../store/adSpacesStore';
import { AsyncContent } from '../../../shared/components/AsyncContent';
import { AdSpaceActions } from './AdSpaceActions';
import { formatCurrencyEUR } from '../../../shared/utils/format';

interface AdSpaceListProps {
  adSpaces: AdSpace[];
  loading: boolean;
  error: string | null;
  sortBy: SortField;
  sortOrder: SortOrder;
  onSort: (field: SortField) => void;
  onBookNow: (space: AdSpace) => void;
  onEdit: (space: AdSpace) => void;
  onDelete: (space: AdSpace) => void;
}

export function AdSpaceList({
  adSpaces,
  loading,
  error,
  sortBy,
  sortOrder,
  onSort,
  onBookNow,
  onEdit,
  onDelete,
}: AdSpaceListProps) {
  const createSortHandler = (field: SortField) => () => {
    onSort(field);
  };

  return (
    <AsyncContent
      loading={loading}
      error={error}
      isEmpty={adSpaces.length === 0}
      emptyMessage="No ad spaces found. Try adjusting your filters."
    >
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                <TableSortLabel
                  active={sortBy === 'name'}
                  direction={sortBy === 'name' ? sortOrder : 'asc'}
                  onClick={createSortHandler('name')}
                >
                  Name
                </TableSortLabel>
              </TableCell>
              <TableCell>
                <TableSortLabel
                  active={sortBy === 'city'}
                  direction={sortBy === 'city' ? sortOrder : 'asc'}
                  onClick={createSortHandler('city')}
                >
                  City
                </TableSortLabel>
              </TableCell>
              <TableCell>
                <TableSortLabel
                  active={sortBy === 'type'}
                  direction={sortBy === 'type' ? sortOrder : 'asc'}
                  onClick={createSortHandler('type')}
                >
                  Type
                </TableSortLabel>
              </TableCell>
              <TableCell align="right">
                <TableSortLabel
                  active={sortBy === 'pricePerDay'}
                  direction={sortBy === 'pricePerDay' ? sortOrder : 'asc'}
                  onClick={createSortHandler('pricePerDay')}
                >
                  Price / Day
                </TableSortLabel>
              </TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {adSpaces.map((space) => (
              <TableRow key={space.id} hover>
                <TableCell>
                  <Typography variant="body1" fontWeight="medium">
                    {space.name}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {space.address}
                  </Typography>
                </TableCell>
                <TableCell>{space.city}</TableCell>
                <TableCell>{space.type}</TableCell>
                <TableCell align="right">
                  <Typography variant="body1" fontWeight="medium">
                    {formatCurrencyEUR(space.pricePerDay)}
                  </Typography>
                </TableCell>
                <TableCell>
                  <Typography
                    variant="body2"
                    color={
                      space.status === 'AVAILABLE'
                        ? 'success.main'
                        : space.status === 'BOOKED'
                        ? 'warning.main'
                        : 'text.secondary'
                    }
                  >
                    {space.status}
                  </Typography>
                </TableCell>
                <TableCell align="right">
                  <AdSpaceActions
                    onBookNow={() => onBookNow(space)}
                    onEdit={() => onEdit(space)}
                    onDelete={() => onDelete(space)}
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


