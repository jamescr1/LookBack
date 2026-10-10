import { Link } from "react-router-dom";

import { Card, CardContent, Typography, CardActions, Button } from '@mui/material';

import type { Investment } from '../../types/investment';

interface InvestmentCardProps {
  investment: Investment;
}

function InvestmentCard({ investment }: InvestmentCardProps) {
  return (
    <Card >
      <CardContent>
        <Typography variant="h6">
          {investment.ticker}
        </Typography>

        <Typography>
          {investment.name}
        </Typography>

        <Typography>
          Provider: {investment.provider}
        </Typography>

        <Typography>
          Exchange: {investment.exchange}
        </Typography>

        <Typography>
          Region: {investment.region}
        </Typography>
      </CardContent>
      <CardActions sx={{ justifyContent: 'flex-end' }}>
         <Button component={Link} to={`/investments/${investment.ticker}`} size="small" variant="contained">
            View Investment
         </Button>
      </CardActions>
    </Card>
  );
}

export default InvestmentCard;