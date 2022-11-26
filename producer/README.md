# Kafka Producer

The producer application is responsible for generating data and sending it to Kafka from the following sources:

- Finnhub API
- Finnhub WSS
- OpenExchangeRates API

# Build

Create the image:

```bash
docker build -t producer:1.0 .
```

Tag the image:

```bash
docker tag producer:1.0 <your-tag>/producer:1.0
```

Push the image:

```bash
docker push <your-tag>/producer:1.0
```

# Run

The necessary environment variables are:

- `FINNHUB_API_KEY` - Finnhub API key
- `OPEN_EXCHANGE_RATES_API_KEY` - OpenExchangeRates API key

Run the image:

```bash
docker run -e FINNHUB_API_KEY=<FINNHUB_API_KEY> -e OPEN_EXCHANGE_RATES_API_KEY=<OPEN_EXCHANGE_RATES_API_KEY> producer:1.0
```
