package co.edu.escuelaing

import clients.finnhub.FinnhubClient
import clients.openexchange.OpenExchangeClient
import clients.openexchange.OpenExchangeClient.Strategies._
import config.Config._
import kafka.config.Config.EXCHANGE_TOPIC
import kafka.producer.{ExchangeRatesProducer, TradesProducer}
import modifiers.ExchangeRatesModifier

import com.typesafe.scalalogging.Logger

object Main {

  val LOGGER: Logger = Logger("Main")

  def main(args: Array[String]): Unit = {

    val finnhubTradesThread = new Thread {
      override def run(): Unit = {
        LOGGER.info("Starting finnhub trades thread")
        sys.env.get(FINNHUB_TOKEN) match {
          case Some(token) =>
            val finnhubClient = new FinnhubClient(token)
            // this runs forever
            finnhubClient.tradesWebSocket(TradesProducer, DEFAULT_SUBSCRIPTIONS)
          case None => LOGGER.warn(s"${FINNHUB_TOKEN} environment variable is needed")
        }
      }
    }

    val exchangeRatesThread = new Thread {
      override def run(): Unit = {
        LOGGER.info("Starting exchange rates thread")
        sys.env.get(EXCHANGE_TOKEN) match {
          case Some(token) =>
            val exchangeClient = new OpenExchangeClient(token)
            val exchangeRateProducer = ExchangeRatesProducer
            val exchangeRatesModifier = ExchangeRatesModifier
            while (true) {
              val exceptionOrRates = exchangeClient.exchangeRates(Cache)
              exceptionOrRates match {
                case Right(rates) =>
                  val modRates = exchangeRatesModifier.modifyFixed(rates)
                  exchangeRateProducer.send(EXCHANGE_TOPIC, modRates.base, modRates.toProto)
                case Left(exception) =>
                  LOGGER.error("Error getting exchange rates", exception)
              }
              Thread.sleep(200)
            }
          case None => LOGGER.warn(s"${EXCHANGE_TOKEN} environment variable is needed")
        }
      }
    }

//    finnhubTradesThread.start()
    exchangeRatesThread.start()
    exchangeRatesThread.join()
//    finnhubTradesThread.join()
  }
}