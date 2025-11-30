import { useEffect } from 'react';
import type { AdSpace } from '../../../api/types';
import {
  type AdSpaceTypeFilter,
  type AdSpacesState,
  type SortField,
  type SortOrder,
  useAdSpacesStore,
} from '../store/adSpacesStore';

export function useAdSpaces() {
  const {
    adSpaces,
    cityFilter,
    typeFilter,
    sortBy,
    sortOrder,
    loading,
    error,
    setCityFilter,
    setTypeFilter,
    setSorting,
    loadAdSpaces,
  } = useAdSpacesStore((state: AdSpacesState) => ({
    adSpaces: state.adSpaces,
    cityFilter: state.cityFilter,
    typeFilter: state.typeFilter,
    sortBy: state.sortBy,
    sortOrder: state.sortOrder,
    loading: state.loading,
    error: state.error,
    setCityFilter: state.setCityFilter,
    setTypeFilter: state.setTypeFilter,
    setSorting: state.setSorting,
    loadAdSpaces: state.loadAdSpaces,
  }));

  useEffect(() => {
    void loadAdSpaces();
  }, [loadAdSpaces]);

  useEffect(() => {
    void loadAdSpaces();
  }, [cityFilter, typeFilter, sortBy, sortOrder, loadAdSpaces]);

  const handleCityFilterChange = (value: string) => {
    setCityFilter(value);
  };

  const handleTypeFilterChange = (value: AdSpaceTypeFilter) => {
    setTypeFilter(value);
  };

  const handleRefresh = () => {
    void loadAdSpaces();
  };

  const handleSort = (field: SortField) => {
    const newOrder: SortOrder =
      sortBy === field && sortOrder === 'asc' ? 'desc' : 'asc';
    setSorting(field, newOrder);
  };

  const hasResults = adSpaces.length > 0;

  const availableCities = Array.from(
    new Set<string>(adSpaces.map((space: AdSpace) => space.city)),
  ).sort();

  const typeOptions: AdSpaceTypeFilter[] = [
    'ALL',
    'BILLBOARD',
    'BUS_STOP',
    'MALL_DISPLAY',
    'TRANSIT_AD',
  ];

  return {
    adSpaces,
    cityFilter,
    typeFilter,
    sortBy,
    sortOrder,
    loading,
    error,
    hasResults,
    availableCities,
    typeOptions,
    handleCityFilterChange,
    handleTypeFilterChange,
    handleRefresh,
    handleSort,
  };
}

