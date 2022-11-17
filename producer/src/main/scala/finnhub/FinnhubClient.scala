package co.edu.escuelaing
package finnhub

import schema.{RecommendationTrend, SymbolLookup, SymbolLookupResult}

import io.circe.Decoder


case class FinnhubClient(token: String) {

  import finnhub.FinnhubClient.{FOREX_EXCHANGE_ENDPOINT, RECOMMENDATION_TRENDS_ENDPOINT, SYMBOL_LOOKUP_ENDPOINT, recommendationTrendDecoder, symbolLookupDecoder}

  import akka.Done
  import akka.actor.ActorSystem
  import akka.http.scaladsl.Http
  import akka.http.scaladsl.model._
  import akka.http.scaladsl.model.ws._
  import akka.stream.scaladsl._
  import io.circe.parser._
  import sttp.client3._

  import scala.concurrent.Future

  val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()

  /**
   * Get forex exchanges from https://finnhub.io/api/v1/forex/exchange
   *
   * @return
   */
  def forexExchanges(): Either[Exception, List[String]] = {
    val url = s"$FOREX_EXCHANGE_ENDPOINT?token=$token"
    basicRequest.get(uri"$url").send(backend).body match {
      case Right(body) => decode[List[String]](body)
      case Left(error) => Left(new Exception(error))
    }
  }

  /**
   * Search symbols from https://finnhub.io/api/v1/search
   *
   * @param symbol
   * @return
   */
  def symbolLookup(symbol: String): Either[Exception, SymbolLookup] = {
    val url = s"$SYMBOL_LOOKUP_ENDPOINT?q=$symbol&token=$token"
    basicRequest.get(uri"$url").send(backend).body match {
      case Right(body) => decode[SymbolLookup](body)
      case Left(error) => Left(new Exception(error))
    }
  }

  // Stock estimates

  /**
   * Get recommendation trends from https://finnhub.io/api/v1/stock/recommendation
   *
   * @param symbol
   * @return
   */
  def recommendationTrends(symbol: String): Either[Exception, List[RecommendationTrend]] = {
    val url = s"$RECOMMENDATION_TRENDS_ENDPOINT?symbol=$symbol&token=$token"
    basicRequest.get(uri"$url").send(backend).body match {
      case Right(body) => decode[List[RecommendationTrend]](body)
      case Left(error) => Left(new Exception(error))
    }
  }
}

object FinnhubClient {
  def apply(token: String): FinnhubClient = new FinnhubClient(token)

  lazy val FOREX_EXCHANGE_ENDPOINT = "https://finnhub.io/api/v1/forex/exchange"
  lazy val SYMBOL_LOOKUP_ENDPOINT = "https://finnhub.io/api/v1/search"
  lazy val RECOMMENDATION_TRENDS_ENDPOINT = "https://finnhub.io/api/v1/stock/recommendation"

  implicit val resultDecoder: Decoder[SymbolLookupResult] = Decoder.forProduct4(
    "description",
    "displaySymbol",
    "symbol",
    "type")(SymbolLookupResult.apply)

  implicit val symbolLookupDecoder: Decoder[SymbolLookup] = Decoder.instance {
    cursor =>
      for {
        count <- cursor.get[Int]("count")
        result <- cursor.downField("result").as[List[SymbolLookupResult]]
      } yield SymbolLookup(count, result)
  }

  implicit val recommendationTrendDecoder: Decoder[RecommendationTrend] = Decoder.forProduct7(
    "period",
    "strongBuy",
    "buy",
    "hold",
    "sell",
    "strongSell",
    "symbol")(RecommendationTrend.apply)
}


