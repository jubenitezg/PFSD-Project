package co.edu.escuelaing
package config

import schema.Subscription

object Config {

  val FINNHUB_TOKEN: String = "FINNHUB_TOKEN"
  val EXCHANGE_TOKEN: String = "OPEN_EXCHANGE_APP_ID"

  val DEFAULT_SUBSCRIPTIONS: List[Subscription] = List(
    Subscription("AAPL"),
    Subscription("AMZN"),
    Subscription("BINANCE:BTCUSDT")
  )

}
