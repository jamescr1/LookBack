import type { Investment } from './investment';

export interface PortfolioInvestment {
    investment: Investment;
    amount: number;
}