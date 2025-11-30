import React from 'react';
import {
  Box,
  Button,
  IconButton,
  InputAdornment,
  TextField,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import RefreshIcon from '@mui/icons-material/Refresh';

interface AdSpacesFiltersProps {
  cityFilter: string;
  loading: boolean;
  onCityChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  onSearch: () => void;
  onRefresh: () => void;
}

export function AdSpacesFilters({
  cityFilter,
  loading,
  onCityChange,
  onSearch,
  onRefresh,
}: AdSpacesFiltersProps) {
  return (
    <Box
      mb={4}
      display="flex"
      flexDirection={{ xs: 'column', sm: 'row' }}
      gap={2}
      alignItems={{ xs: 'stretch', sm: 'center' }}
    >
      <TextField
        label="Filter by city"
        value={cityFilter}
        onChange={onCityChange}
        variant="outlined"
        size="small"
        sx={{ maxWidth: 320 }}
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <IconButton
                aria-label="search"
                onClick={onSearch}
                edge="end"
                disabled={loading}
              >
                <SearchIcon />
              </IconButton>
            </InputAdornment>
          ),
        }}
      />
      <Box flexGrow={1} />
      <Button
        variant="outlined"
        startIcon={<RefreshIcon />}
        onClick={onRefresh}
        disabled={loading}
      >
        Reload
      </Button>
    </Box>
  );
}


