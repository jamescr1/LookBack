INSERT INTO investment (
    ticker,
    name,
    provider,
    exchange,
    provider_symbol,
    trading_currency,
    region
)
VALUES
    (
        'VUSA',
        'Vanguard S&P 500 UCITS ETF',
        'Vanguard',
        'LSE',
        'VUSA.LON',
        'GBP',
        'US'
    ),
    (
        'VWRL',
        'Vanguard FTSE All-World UCITS ETF',
        'Vanguard',
        'LSE',
        'VWRL.LON',
        'GBP',
        'Global'
    ),
    (
        'SWDA',
        'iShares Core MSCI World UCITS ETF',
        'iShares',
        'LSE',
        'SWDA.LON',
        'GBP',
        'Developed markets'
    ),
    (
        'EMIM',
        'iShares Core MSCI Emerging Markets IMI UCITS ETF',
        'iShares',
        'LSE',
        'EMIM.LON',
        'GBP',
        'Emerging markets'
    )
ON CONFLICT (exchange, ticker) DO NOTHING;