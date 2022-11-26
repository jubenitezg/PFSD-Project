package co.edu.escuelaing

import kafka.config.Config.ssc
import kafka.consumer.{ExchangeConsumer, TradesConsumer}

object Main {
  def main(args: Array[String]): Unit = {
    val activateExchangeConsumer = sys.env.getOrElse("EXCHANGE_CONSUMER", "true").toBoolean
    val activateTradesConsumer = sys.env.getOrElse("TRADES_CONSUMER", "false").toBoolean

    if (activateExchangeConsumer) {
      ExchangeConsumer.consume()
    }
    if (activateTradesConsumer) {
      TradesConsumer.consume()
    }
    ssc.start()
    ssc.awaitTermination()
  }
}