import React from 'react';
import { Box, Tabs, Tab } from '@mui/material';
import StorefrontIcon from '@mui/icons-material/Storefront';
import BookIcon from '@mui/icons-material/Book';

interface NavigationProps {
  currentTab: 'adSpaces' | 'bookings';
  onTabChange: (tab: 'adSpaces' | 'bookings') => void;
}

export function Navigation({ currentTab, onTabChange }: NavigationProps) {
  return (
    <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 3 }}>
      <Tabs
        value={currentTab}
        onChange={(_, newValue) => onTabChange(newValue)}
        aria-label="navigation tabs"
      >
        <Tab
          value="adSpaces"
          label="Ad Spaces"
          icon={<StorefrontIcon />}
          iconPosition="start"
        />
        <Tab
          value="bookings"
          label="Booking Requests"
          icon={<BookIcon />}
          iconPosition="start"
        />
      </Tabs>
    </Box>
  );
}

