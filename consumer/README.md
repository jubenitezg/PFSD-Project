# Kafka Consumer

The consumer application is responsible for consuming data from Kafka and sending it to Spark Streaming. Spark Streaming
is responsible for processing the data and sending it to the corresponding service.

It has two main components:

- Trades Consumer
- Currencies Consumer (Exchange Rates)

Each one can send an email depending on the defined conditions.

# Build

Create the image:

```bash
docker build -t consumer:1.0 .
```

Tag the image:

```bash
docker tag consumer:1.0 <your-tag>/consumer:1.0
```

Push the image:

```bash
docker push <your-tag>/consumer:1.0
```

# Run

The necessary environment variables are:

- `EMAIL_USER` - Email address to send the email from
- `EMAIL_PASSWORD` - Password for the email address
- `RECIPIENT` - Email address to send the email to (default: same as `EMAIL_USER`)
- `EXCHANGE_CONSUMER` - Use exchange consumer (default: `true`)
- `TRADES_CONSUMER` - Use trades consumer(default: `true`)

Run the image:

```bash
docker run -e EMAIL_USER=<EMAIL_USER> -e EMAIL_PASSWORD=<EMAIL_PASSWORD> -e RECIPIENT=<RECIPIENT> consumer:1.0
```
