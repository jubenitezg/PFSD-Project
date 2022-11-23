package co.edu.escuelaing

import clients.finnhub.FinnhubClient
import clients.openexchange.OpenExchangeClient
import clients.openexchange.OpenExchangeClient.Strategies._
import config.Config
import kafka.producer.TradesProducer

import com.typesafe.scalalogging.Logger

object Main {

  val LOGGER: Logger = Logger("Main")

  def main(args: Array[String]): Unit = {
//    sys.env.get(Config.FINNHUB_TOKEN) match {
//      case Some(token) =>
//        val finnhubClient = new FinnhubClient(token)
//        // The websocket spawns a new thread
//        val promise = finnhubClient.tradesWebSocket(TradesProducer, Config.DEFAULT_SUBSCRIPTIONS)
//      case None => LOGGER.warn(s"${Config.FINNHUB_TOKEN} environment variable is needed")
//    }
    sys.env.get(Config.EXCHANGE_TOKEN) match {
      case Some(token) =>
        val exchangeClient = new OpenExchangeClient(token)
        println(exchangeClient.exchangeRates(Cache))
      case None => LOGGER.warn(s"${Config.EXCHANGE_TOKEN} environment variable is needed")
    }
  }
}