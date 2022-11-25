package co.edu.escuelaing

import kafka.consumer.ExchangeConsumer

object Main {
  def main(args: Array[String]): Unit = {
    val exchangeConsumerThread = new Thread {
      override def run(): Unit = {
        ExchangeConsumer.consume()
      }
    }

    exchangeConsumerThread.start()
    exchangeConsumerThread.join()
  }
}