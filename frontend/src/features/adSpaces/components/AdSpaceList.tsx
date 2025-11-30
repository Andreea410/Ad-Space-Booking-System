import React from 'react';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import type { AdSpace } from '../../../api/types';
import { AsyncContent } from '../../../shared/components/AsyncContent';
import { AdSpaceCard } from './AdSpaceCard';
import { AdSpaceActions } from './AdSpaceActions';

interface AdSpaceListProps {
  adSpaces: AdSpace[];
  loading: boolean;
  error: string | null;
  onBookNow: (space: AdSpace) => void;
  onEdit: (space: AdSpace) => void;
  onDelete: (space: AdSpace) => void;
}

export function AdSpaceList({
  adSpaces,
  loading,
  error,
  onBookNow,
  onEdit,
  onDelete,
}: AdSpaceListProps) {
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
              <TableCell>Ad Space</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {adSpaces.map((space) => (
              <TableRow key={space.id}>
                <TableCell>
                  <AdSpaceCard space={space} />
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


