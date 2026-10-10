import type { PortfolioInvestment } from "../types/portfolioInvestment";

interface portfolioPageProps {
  portfolio: PortfolioInvestment[]
}

function PortfolioPage({ portfolio }: portfolioPageProps ) {
  return(
    <>
    <h1>Current portfolio:</h1>
    {
      portfolio.map((currPortfolioInv) => (
        <div key={currPortfolioInv.investment.ticker}>
          <p>{currPortfolioInv.investment.ticker}</p>
          <p>{currPortfolioInv.amount}</p>
        </div>
    ))
    }
    </>
  )
}

export default PortfolioPage;
