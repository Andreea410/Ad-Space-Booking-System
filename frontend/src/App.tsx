import React, { useState } from 'react';
import './App.css';
import { AppLayout } from './components/layout/AppLayout';
import { Navigation } from './components/layout/Navigation';
import { AdSpacesPage } from './features/adSpaces/components/AdSpacesPage';
import { BookingsPage } from './features/bookings/components/BookingsPage';

function App() {
  const [currentTab, setCurrentTab] = useState<'adSpaces' | 'bookings'>('adSpaces');

  return (
    <AppLayout>
      <Navigation currentTab={currentTab} onTabChange={setCurrentTab} />
      {currentTab === 'adSpaces' ? <AdSpacesPage /> : <BookingsPage />}
    </AppLayout>
  );
}

export default App;
