import { useEffect } from 'react';
import type { AdSpace } from '../../../api/types';
import {
  type AdSpaceTypeFilter,
  type AdSpacesState,
  useAdSpacesStore,
} from '../store/adSpacesStore';

export function useAdSpaces() {
  const {
    adSpaces,
    cityFilter,
    typeFilter,
    loading,
    error,
    setCityFilter,
    setTypeFilter,
    loadAdSpaces,
  } = useAdSpacesStore((state: AdSpacesState) => ({
    adSpaces: state.adSpaces,
    cityFilter: state.cityFilter,
    typeFilter: state.typeFilter,
    loading: state.loading,
    error: state.error,
    setCityFilter: state.setCityFilter,
    setTypeFilter: state.setTypeFilter,
    loadAdSpaces: state.loadAdSpaces,
  }));

  useEffect(() => {
    void loadAdSpaces();
  }, [loadAdSpaces]);

  useEffect(() => {
    void loadAdSpaces();
  }, [cityFilter, typeFilter, loadAdSpaces]);

  const handleCityFilterChange = (value: string) => {
    setCityFilter(value);
  };

  const handleTypeFilterChange = (value: AdSpaceTypeFilter) => {
    setTypeFilter(value);
  };

  const handleRefresh = () => {
    void loadAdSpaces();
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
    loading,
    error,
    hasResults,
    availableCities,
    typeOptions,
    handleCityFilterChange,
    handleTypeFilterChange,
    handleRefresh,
  };
}

