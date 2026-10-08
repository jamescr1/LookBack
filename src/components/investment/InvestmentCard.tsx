import { Card, CardContent, Typography } from '@mui/material';

import type { Investment } from '../../types/investment';

interface InvestmentCardProps {
  investment: Investment;
}

function InvestmentCard({ investment }: InvestmentCardProps) {
  return (
    <Card>
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
      </CardContent>
    </Card>
  );
}

export default InvestmentCard;