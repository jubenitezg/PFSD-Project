package co.edu.escuelaing
package kafka.consumer

import protos.exchange_rates.ExchangeRatesProto

object ExchangeConsumer extends Consumer[ExchangeRatesProto] {
  override def process(topic: String, key: String, value: ExchangeRatesProto): Unit = {
    println(s"topic: $topic, key: $key, value: $value")
  }
}
