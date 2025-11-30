import { create, type StateCreator } from 'zustand';
import { fetchAdSpaces } from '../../../api/adSpaces';
import type { AdSpace, AdSpaceType } from '../../../api/types';

export type AdSpaceTypeFilter = 'ALL' | AdSpaceType;

export interface AdSpacesState {
  adSpaces: AdSpace[];
  loading: boolean;
  error: string | null;
  cityFilter: string;
  typeFilter: AdSpaceTypeFilter;
  setCityFilter: (city: string) => void;
  setTypeFilter: (type: AdSpaceTypeFilter) => void;
  loadAdSpaces: () => Promise<void>;
  deleteAdSpace: (id: number) => void;
  markAsBooked: (id: number) => void;
  updateAdSpace: (
    id: number,
    updates: Partial<Pick<AdSpace, 'name' | 'city' | 'address' | 'pricePerDay' | 'type'>>,
  ) => void;
}

const createAdSpacesStore: StateCreator<AdSpacesState> = (set, get) => ({
  adSpaces: [],
  loading: false,
  error: null,
  cityFilter: '',
  typeFilter: 'ALL',

  setCityFilter: (city: string) => set({ cityFilter: city }),

  setTypeFilter: (type: AdSpaceTypeFilter) => set({ typeFilter: type }),

  loadAdSpaces: async () => {
    const { cityFilter, typeFilter } = get();
    try {
      set({ loading: true, error: null });
      const data = await fetchAdSpaces({
        city: cityFilter || undefined,
        type: typeFilter !== 'ALL' ? typeFilter : undefined,
      });
      set({ adSpaces: data });
    } catch (err) {
      set({
        error: err instanceof Error ? err.message : 'Failed to load ad spaces',
      });
    } finally {
      set({ loading: false });
    }
  },

  deleteAdSpace: (id: number) =>
    set((state: AdSpacesState) => ({
      adSpaces: state.adSpaces.filter((space: AdSpace) => space.id !== id),
    })),

  markAsBooked: (id: number) =>
    set((state: AdSpacesState) => ({
      adSpaces: state.adSpaces.map((space: AdSpace) =>
        space.id === id ? { ...space, status: 'BOOKED' } : space,
      ),
    })),

  updateAdSpace: (
    id: number,
    updates: Partial<Pick<AdSpace, 'name' | 'city' | 'address' | 'pricePerDay' | 'type'>>,
  ) =>
    set((state: AdSpacesState) => ({
      adSpaces: state.adSpaces.map((space: AdSpace) =>
        space.id === id ? { ...space, ...updates } : space,
      ),
    })),
});

export const useAdSpacesStore = create<AdSpacesState>(createAdSpacesStore);


