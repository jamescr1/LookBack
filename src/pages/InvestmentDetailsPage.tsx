import { useParams } from 'react-router-dom';

import { mockData } from '../data/mockData';

function InvestmentDetailsPage() {

    const { ticker } = useParams();

    const investment = mockData.find(currInvestment => currInvestment.ticker === ticker)

    if(!investment) {
      return (<h1>Investment not found</h1>)
    } else {
      return (<h1>{investment.name}</h1>)
    }
}

export default InvestmentDetailsPage;
