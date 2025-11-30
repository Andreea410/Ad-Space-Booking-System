import { useCallback, useEffect, useState, ChangeEvent } from 'react';
import { fetchAdSpaces } from '../../../api/adSpaces';
import type { AdSpace } from '../../../api/types';

export function useAdSpaces() {
  const [adSpaces, setAdSpaces] = useState<AdSpace[]>([]);
  const [cityFilter, setCityFilter] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const loadAdSpaces = useCallback(
    async (opts?: { city?: string }) => {
      try {
        setLoading(true);
        setError(null);
        const data = await fetchAdSpaces({
          city: opts?.city ?? (cityFilter || undefined),
        });
        setAdSpaces(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load ad spaces');
      } finally {
        setLoading(false);
      }
    },
    [cityFilter],
  );

  useEffect(() => {
    void loadAdSpaces();
  }, [loadAdSpaces]);

  const handleCityChange = (event: ChangeEvent<HTMLInputElement>) => {
    setCityFilter(event.target.value);
  };

  const handleSearch = () => {
    void loadAdSpaces({ city: cityFilter || undefined });
  };

  const handleRefresh = () => {
    void loadAdSpaces();
  };

  const hasResults = adSpaces.length > 0;

  return {
    adSpaces,
    cityFilter,
    loading,
    error,
    hasResults,
    handleCityChange,
    handleSearch,
    handleRefresh,
  };
}


