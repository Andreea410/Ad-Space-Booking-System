import React from 'react';
import {
  TableHead,
  TableRow,
  TableCell,
  TableSortLabel,
} from '@mui/material';

export interface TableColumn<T extends string = string> {
  id: T;
  label: string;
  sortable?: boolean;
  align?: 'left' | 'center' | 'right';
  minWidth?: number;
}

interface SortableTableHeadProps<T extends string = string> {
  columns: TableColumn<T>[];
  sortBy?: T | null;
  sortOrder?: 'asc' | 'desc';
  onSort?: (column: T) => void;
}

export function SortableTableHead<T extends string = string>({
  columns,
  sortBy,
  sortOrder = 'asc',
  onSort,
}: SortableTableHeadProps<T>) {
  const handleSort = (columnId: T) => {
    if (onSort) {
      onSort(columnId);
    }
  };

  return (
    <TableHead>
      <TableRow>
        {columns.map((column) => (
          <TableCell
            key={column.id}
            align={column.align || 'left'}
            sx={{ minWidth: column.minWidth, fontWeight: 'bold' }}
          >
            {column.sortable && onSort ? (
              <TableSortLabel
                active={sortBy === column.id}
                direction={sortBy === column.id ? sortOrder : 'asc'}
                onClick={() => handleSort(column.id)}
              >
                {column.label}
              </TableSortLabel>
            ) : (
              column.label
            )}
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}

