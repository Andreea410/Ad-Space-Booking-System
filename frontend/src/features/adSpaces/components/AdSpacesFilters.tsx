import React from 'react';
import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  type SelectChangeEvent,
} from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import type { AdSpaceTypeFilter } from '../store/adSpacesStore';

interface AdSpacesFiltersProps {
  cityFilter: string;
  typeFilter: AdSpaceTypeFilter;
  cityOptions: string[];
  typeOptions: AdSpaceTypeFilter[];
  loading: boolean;
  onCityChange: (value: string) => void;
  onTypeChange: (value: AdSpaceTypeFilter) => void;
  onRefresh: () => void;
}

export function AdSpacesFilters({
  cityFilter,
  typeFilter,
  cityOptions,
  typeOptions,
  loading,
  onCityChange,
  onTypeChange,
  onRefresh,
}: AdSpacesFiltersProps) {
  const handleCitySelectChange = (event: SelectChangeEvent<string>) => {
    onCityChange(event.target.value);
  };

  const handleTypeSelectChange = (event: SelectChangeEvent<string>) => {
    onTypeChange(event.target.value as AdSpaceTypeFilter);
  };

  return (
    <Box
      mb={4}
      display="flex"
      flexDirection={{ xs: 'column', sm: 'row' }}
      gap={2}
      alignItems={{ xs: 'stretch', sm: 'center' }}
    >
      <FormControl size="small" sx={{ minWidth: 160 }}>
        <InputLabel id="ad-space-type-label">Type</InputLabel>
        <Select
          labelId="ad-space-type-label"
          value={typeFilter}
          label="Type"
          onChange={handleTypeSelectChange}
          disabled={loading}
        >
          {typeOptions.map((type) => (
            <MenuItem key={type} value={type}>
              {type === 'ALL' ? 'All types' : type}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <FormControl size="small" sx={{ minWidth: 180 }}>
        <InputLabel id="ad-space-city-label">City</InputLabel>
        <Select
          labelId="ad-space-city-label"
          value={cityFilter}
          label="City"
          onChange={handleCitySelectChange}
          disabled={loading || cityOptions.length === 0}
        >
          <MenuItem value="">
            <em>All cities</em>
          </MenuItem>
          {cityOptions.map((city) => (
            <MenuItem key={city} value={city}>
              {city}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

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