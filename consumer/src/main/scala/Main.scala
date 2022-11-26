package co.edu.escuelaing

import kafka.consumer.{ExchangeConsumer, TradesConsumer}

object Main {
  def main(args: Array[String]): Unit = {
    val tradesConsumerThread = new Thread {
      override def run(): Unit = {
        TradesConsumer.consume()
      }
    }


    val exchangeConsumerThread = new Thread {
      override def run(): Unit = {
        ExchangeConsumer.consume()
      }
    }

    val activateExchangeConsumer = sys.env.getOrElse("EXCHANGE_CONSUMER", "true").toBoolean
    val activateTradesConsumer = sys.env.getOrElse("TRADES_CONSUMER", "true").toBoolean

    if (activateExchangeConsumer) {
      exchangeConsumerThread.start()
    }
    if (activateTradesConsumer) {
      tradesConsumerThread.start()
    }
    if (activateExchangeConsumer) {
      exchangeConsumerThread.join()
    }
    if (activateTradesConsumer) {
      tradesConsumerThread.join()
    }
  }
}