import { Typography } from '@mui/material';

import { mockData } from '../data/mockData';
import InvestmentCard from '../components/investment/InvestmentCard';

function InvestmentSelectionPage() {
  return (
    <>
      <Typography variant="h4">
        Investment Explorer
      </Typography>

      {mockData.map((investment) => (
        <InvestmentCard 
          key={investment.ticker} 
          investment={investment} 
        />
      ))}
    </>
  );
}

export default InvestmentSelectionPage;
