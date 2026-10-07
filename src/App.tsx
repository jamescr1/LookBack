import { BrowserRouter, Routes, Route } from 'react-router-dom';

import HomePage from './pages/HomePage';
import InvestmentSelectionPage from './pages/InvestmentSelectionPage';
import InvestmentDetailsPage from './pages/InvestmentDetailsPage';
import PortfolioPage from './pages/PortfolioPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/investments" element={<InvestmentSelectionPage />} />
        <Route
          path="/investments/:ticker"
          element={<InvestmentDetailsPage />}
        />
        <Route path="/portfolio" element={<PortfolioPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
