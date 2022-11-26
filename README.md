# Kafka and Spark Streaming for Analyzing Stocks, Currencies, and Crypto Data

The project is composed of 3 main components: 
- Kafka
- Spark Streaming
- Consumer and Producer applications

The image shows at a high level the architecture of the project. 
The producer application is responsible for generating data and sending it to Kafka. 
The consumer application is responsible for consuming data from Kafka and sending it to Spark Streaming. 
Spark Streaming is responsible for processing the data and sending it to the corresponding service.

![](assets/project-archiquecture.png)

More information about the [Producer](./producer/README.md) and [Consumer](./consumer/README.md) applications can be found in their respective README files.

# Pre-requisites
- Docker
- Java 11
- sbt
- Scala
- Finnhub Account
- OpenExchangeRates Account

# Build

Use `docker compose up` to build and run the project.

> **_NOTE:_** The project's dockers are built for M1 chip macs. If you are using a different architecture, you will need to build the images yourself. 
> Each application has its own Dockerfile with instructions on how to build.

The necessary environment variables are set in the `docker-compose.yml` file:
- `FINNHUB_API_KEY` - Finnhub API key
- `OPEN_EXCHANGE_RATES_API_KEY` - OpenExchangeRates API key
- `EMAIL_USER` - Email address to send the email from
- `EMAIL_PASSWORD` - Password for the email address
- `RECIPIENT` - Email address to send the email to (default: same as `EMAIL_USER`)
- `EXCHANGE_CONSUMER` - Use exchange consumer (default: `true`)
- `TRADES_CONSUMER` - Use trades consumer(default: `true`)

# License

GNU GENERAL PUBLIC LICENSE [LICENSE.md](LICENSE.md)

# Authors

* Julián Benítez Gutiérrez - *Development* - [julianbenitez99](https://github.com/julianbenitez99)

