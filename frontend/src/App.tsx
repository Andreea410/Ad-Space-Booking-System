import React from 'react';
import './App.css';
import { AppLayout } from './components/layout/AppLayout';
import { AdSpacesPage } from './features/adSpaces/components/AdSpacesPage';

function App() {
  return (
    <AppLayout>
      <AdSpacesPage />
    </AppLayout>
  );
}

export default App;
