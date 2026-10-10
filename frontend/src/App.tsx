import { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import HomePage from './pages/HomePage';
import InvestmentSelectionPage from './pages/InvestmentSelectionPage';
import InvestmentDetailsPage from './pages/InvestmentDetailsPage';
import PortfolioPage from './pages/PortfolioPage';
import Layout from './components/layout/Layout';
import type { PortfolioInvestment } from './types/portfolioInvestment';


function App() {
     const [portfolio, setPortfolio] = useState<PortfolioInvestment[]>([]);

    function addToPortfolio(portfolioInvestment: PortfolioInvestment): void {
      const investmentAlreadyInPort = portfolio.some((currInv) => currInv.investment.ticker === portfolioInvestment.investment.ticker);
      if(investmentAlreadyInPort) {
         return;
      } 
      setPortfolio(prevPortfolio => [...prevPortfolio, portfolioInvestment])
   }

  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/investments" element={<InvestmentSelectionPage />} />
          <Route
            path="/investments/:ticker"
            element={<InvestmentDetailsPage addToPortfolio={addToPortfolio}/>}
          />
          <Route path="/portfolio" element={<PortfolioPage portfolio={portfolio}/>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
