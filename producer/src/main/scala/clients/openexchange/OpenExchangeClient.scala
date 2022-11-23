package co.edu.escuelaing
package clients.openexchange

import schema.ExchangeRates

import com.typesafe.scalalogging.Logger
import io.circe.Decoder
import io.circe.jawn.decode
import sttp.client3.{HttpClientSyncBackend, Identity, SttpBackend, UriContext, basicRequest}

import scala.collection.mutable
import scala.io.Source

case class OpenExchangeClient(token: String) {

  import OpenExchangeClient.Strategies._
  import OpenExchangeClient._

  val LOGGER: Logger = Logger("OpenExchangeClient")

  val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()

  val cache: mutable.Map[String, ExchangeRates] = mutable.HashMap[String, ExchangeRates]()

  def exchangeRates(strategy: Strategy): Either[Exception, ExchangeRates] = {
    strategy match {
      case Latest =>
        LOGGER.info("Getting latest exchange rates from OpenExchange")
        val url = s"$EXCHANGE_RATE_ENDPOINT?app_id=$token"
        basicRequest.get(uri"$url").send(backend).body match {
          case Right(body) => decode[ExchangeRates](body)
          case Left(error) => Left(new Exception(error))
        }
      case Cache =>
        LOGGER.info("Getting cached exchange rates from OpenExchange")
        cache.get("exchange_rates") match {
          case Some(exchangeRates) => Right(exchangeRates)
          case None =>
            val resource = Source.fromResource("exchange-latest.json")
            val exchangeRates = decode[ExchangeRates](resource.mkString)
            cache("exchange_rates") = exchangeRates.getOrElse(throw new Exception("Could not decode exchange rates"))
            exchangeRates
        }
    }
  }

}

object OpenExchangeClient {

  object Strategies extends Enumeration {
    type Strategy = Value
    val Latest, Cache = Value
  }

  lazy val EXCHANGE_RATE_ENDPOINT = "https://openexchangerates.org/api/latest.json"

  implicit val exchangeDecoder: Decoder[ExchangeRates] = Decoder.forProduct5(
    "disclaimer",
    "license",
    "timestamp",
    "base",
    "rates"
  )(ExchangeRates.apply)
}

