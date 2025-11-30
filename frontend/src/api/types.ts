export type AdSpaceType = 'BILLBOARD' | 'BUS_STOP' | 'MALL_DISPLAY' | 'TRANSIT_AD';

export type AdSpaceStatus = 'AVAILABLE' | 'BOOKED' | 'MAINTENANCE' | string;

export interface AdSpace {
  id: number;
  name: string;
  type: AdSpaceType;
  city: string;
  address: string;
  pricePerDay: number;
  status: AdSpaceStatus;
}

export type BookingStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | string;

export interface BookingRequest {
  id: number;
  adSpaceId: number;
  advertiserName: string;
  advertiserEmail: string;
  startDate: string;
  endDate: string;
  totalCost: number;
  status: BookingStatus;
}

export interface CreateBookingRequestPayload {
  adSpaceId: number;
  advertiserName: string;
  advertiserEmail: string;
  startDate: string;
  endDate: string;
}


