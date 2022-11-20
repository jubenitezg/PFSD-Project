package co.edu.escuelaing

import config.Config
import finnhub.FinnhubClient
import kafka.producer.TradesProducer

import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main {

  val LOGGER: Logger = Logger("Main")

  def main(args: Array[String]): Unit = {
    sys.env.get(Config.ENV_TOKEN) match {
      case Some(token) =>
        val finnhubClient = new FinnhubClient(token)
        // The websocket spawns a new thread
        val promise = finnhubClient.tradesWebSocket(TradesProducer, Config.DEFAULT_SUBSCRIPTIONS)
      case None => LOGGER.warn(s"${Config.ENV_TOKEN} environment variable is needed")
    }
  }
}