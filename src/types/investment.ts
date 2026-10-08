export type InvestmentType = "ETF" | "ETC";
export type Exchange = "LSE" | "NYSE" | "NASDAQ";

export interface Investment {
    ticker: string;
    name: string;
    provider: string;
    exchange: Exchange;
    investmentType: InvestmentType;
    region: string;
}

