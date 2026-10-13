import { useParams } from 'react-router-dom';
import { Link } from "react-router-dom";

import Button from '@mui/material/Button';

import { mockData } from '../data/mockData';
import type { PortfolioInvestment } from '../types/portfolioInvestment';

type AddToPortfolioFunc = (portfolioInvestment: PortfolioInvestment) => void;
interface InvestmentDetailsPageProps {
    addToPortfolio: AddToPortfolioFunc
}
function InvestmentDetailsPage({ addToPortfolio }: InvestmentDetailsPageProps) {

    const { ticker } = useParams();

    const investment = mockData.find(currInvestment => currInvestment.ticker === ticker)

    if(!investment) {
      return (<h1>Investment not found</h1>)
    } 

    return (
      <>
        <h1>{investment.name}</h1>
        <Button 
          component={Link} 
          to="/portfolio" 
          onClick={() => addToPortfolio({
            investment: investment,
            amount: 1000
          })}
        >
          Add to portfolio
        </Button>
      </>
    )
}

export default InvestmentDetailsPage;
