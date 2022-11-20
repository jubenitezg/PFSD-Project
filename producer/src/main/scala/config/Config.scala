package co.edu.escuelaing
package config

import schema.Subscription

object Config {

  val ENV_TOKEN: String = "FINNHUB_TOKEN"

  val DEFAULT_SUBSCRIPTIONS: List[Subscription] = List(
    Subscription("AAPL"),
    Subscription("AMZN"),
    Subscription("BINANCE:BTCUSDT")
  )

}
