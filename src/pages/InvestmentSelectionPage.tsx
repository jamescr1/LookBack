import { Typography } from '@mui/material';

import { mockData } from '../data/mockData';
import InvestmentCard from '../components/investment/InvestmentCard';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';


function InvestmentSelectionPage() {
  return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h4" sx={{ mb: 3 }}>
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
     </Box>
  );
}

export default InvestmentSelectionPage;
