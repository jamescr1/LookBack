import { Typography } from '@mui/material';

import { mockData } from '../data/mockData';
import InvestmentCard from '../components/investment/InvestmentCard';
import Grid from '@mui/material/Grid';

function InvestmentSelectionPage() {
  return (
    <>
      <Typography variant="h4">
        Investment Explorer
      </Typography>

      <Grid container spacing={2}>
        {mockData.map((investment) => (
          <Grid key={investment.ticker} size={{ xs: 12, sm: 6, md: 4 }}>
            <InvestmentCard 
              investment={investment} 
            />
          </Grid>  
        ))}
      </Grid>
    </>
  );
}

export default InvestmentSelectionPage;
