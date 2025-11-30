import { create, type StateCreator } from 'zustand';
import {
  fetchAdSpaces,
  updateAdSpace as apiUpdateAdSpace,
  deleteAdSpace as apiDeleteAdSpace,
  bookAdSpace as apiBookAdSpace,
} from '../../../api/adSpaces';
import type { AdSpace, AdSpaceType } from '../../../api/types';

export type AdSpaceTypeFilter = 'ALL' | AdSpaceType;
export type SortField = 'name' | 'city' | 'pricePerDay' | 'type';
export type SortOrder = 'asc' | 'desc';

export interface AdSpacesState {
  adSpaces: AdSpace[];
  loading: boolean;
  error: string | null;
  cityFilter: string;
  typeFilter: AdSpaceTypeFilter;
  sortBy: SortField;
  sortOrder: SortOrder;
  setCityFilter: (city: string) => void;
  setTypeFilter: (type: AdSpaceTypeFilter) => void;
  setSorting: (field: SortField, order: SortOrder) => void;
  loadAdSpaces: () => Promise<void>;
  deleteAdSpace: (id: number) => Promise<void>;
  markAsBooked: (id: number) => Promise<void>;
  updateAdSpace: (
    id: number,
    updates: Partial<Pick<AdSpace, 'name' | 'city' | 'address' | 'pricePerDay' | 'type'>>,
  ) => Promise<void>;
}

const createAdSpacesStore: StateCreator<AdSpacesState> = (set, get) => ({
  adSpaces: [],
  loading: false,
  error: null,
  cityFilter: '',
  typeFilter: 'ALL',
  sortBy: 'name',
  sortOrder: 'asc',

  setCityFilter: (city: string) => set({ cityFilter: city }),

  setTypeFilter: (type: AdSpaceTypeFilter) => set({ typeFilter: type }),

  setSorting: (field: SortField, order: SortOrder) =>
    set({ sortBy: field, sortOrder: order }),

  loadAdSpaces: async () => {
    const { cityFilter, typeFilter, sortBy, sortOrder } = get();
    try {
      set({ loading: true, error: null });
      const data = await fetchAdSpaces({
        city: cityFilter || undefined,
        type: typeFilter !== 'ALL' ? typeFilter : undefined,
        sortBy,
        sortOrder,
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

  deleteAdSpace: async (id: number) => {
    try {
      await apiDeleteAdSpace(id);
      set((state: AdSpacesState) => ({
        adSpaces: state.adSpaces.filter((space: AdSpace) => space.id !== id),
      }));
    } catch (err) {
      set({
        error: err instanceof Error ? err.message : 'Failed to delete ad space',
      });
    }
  },

  markAsBooked: async (id: number) => {
    try {
      const updated = await apiBookAdSpace(id);
      set((state: AdSpacesState) => ({
        adSpaces: state.adSpaces.map((space: AdSpace) =>
          space.id === id ? updated : space,
        ),
      }));
    } catch (err) {
      set({
        error: err instanceof Error ? err.message : 'Failed to book ad space',
      });
    }
  },

  updateAdSpace: async (
    id: number,
    updates: Partial<Pick<AdSpace, 'name' | 'city' | 'address' | 'pricePerDay' | 'type'>>,
  ) => {
    try {
      const updated = await apiUpdateAdSpace(id, { name: updates.name! });
      set((state: AdSpacesState) => ({
        adSpaces: state.adSpaces.map((space: AdSpace) =>
          space.id === id ? updated : space,
        ),
      }));
    } catch (err) {
      set({
        error: err instanceof Error ? err.message : 'Failed to update ad space',
      });
    }
  },
});

export const useAdSpacesStore = create<AdSpacesState>(createAdSpacesStore);


