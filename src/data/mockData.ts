import type { Investment } from '../types/investment';

export const mockData: Investment[] = [
  {
    ticker: 'VUSA',
    name: 'Vanguard S&P 500 UCITS ETF',
    provider: 'Vanguard',
    exchange: 'LSE',
    investmentType: 'ETF',
    region: 'United States',
  },
 {
    ticker: 'VWRL',
    name: 'Vanguard FTSE All-World UCITS ETF',
    provider: 'Vanguard',
    exchange: 'LSE',
    investmentType: 'ETF',
    region: 'Global',
  },
  {
    ticker: 'SWDA',
    name: 'iShares Core MSCI World UCITS ETF',
    provider: 'iShares',
    exchange: 'LSE',
    investmentType: 'ETF',
    region: 'Developed Markets',
  },
  {
    ticker: 'EMIM',
    name: 'iShares Core MSCI Emerging Markets IMI UCITS ETF',
    provider: 'iShares',
    exchange: 'LSE',
    investmentType: 'ETF',
    region: 'Emerging Markets',
  },
  {
    ticker: 'IUKD',
    name: 'iShares UK Dividend UCITS ETF',
    provider: 'iShares',
    exchange: 'LSE',
    investmentType: 'ETF',
    region: 'United Kingdom',
  },
  {
    ticker: 'VUKE',
    name: 'Vanguard FTSE 100 UCITS ETF',
    provider: 'Vanguard',
    exchange: 'LSE',
    investmentType: 'ETF',
    region: 'United Kingdom',
  },
];