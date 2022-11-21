package co.edu.escuelaing
package finnhub

import kafka.config.Config
import kafka.producer.Producer
import protos.trades.Trade
import protos.trades.Trade.Data
import schema.{RecommendationTrend, Subscription, SymbolLookup, SymbolLookupResult}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.Logger
import io.circe.Decoder
import scalapb.UnknownFieldSet

import scala.concurrent.{Future, Promise}


case class FinnhubClient(token: String) {

  import finnhub.FinnhubClient._

  import akka.http.scaladsl.model.ws._
  import akka.stream.scaladsl._
  import io.circe.parser._
  import sttp.client3._

  val LOGGER: Logger = Logger("FinnhubClient")

  val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()

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

  /**
   * Real time US stock, forex and crypto from wss://ws.finnhub.io
   * Sends to kafka topic "trades"
   *
   * @param producer
   * @param subscriptions
   * @return
   */
  def tradesWebSocket(producer: Producer[Trade], subscriptions: List[Subscription]): Promise[Option[Nothing]] = {
    import system.dispatcher
    val textMessages = subscriptions.map(s => TextMessage(s.toJson))
    val flow = Flow.fromSinkAndSourceMat(
      Sink.foreach[Message] {
        case tm: TextMessage.Strict =>
          decode[Trade](tm.text) match {
            case Right(trade) =>
              producer.send(Config.TOPIC, trade.`type`, trade)
            case Left(error) => LOGGER.warn(s"Error decoding trade: $error")
          }
        case _ => LOGGER.warn("Unexpected message type")
      },
      Source(textMessages)
        .concatMat(Source.maybe)(Keep.right)
    )(Keep.right)

    val (upgradeResponse, promise) =
      Http().singleWebSocketRequest(
        WebSocketRequest(s"$WEBSOCKET_ENDPOINT?token=$token"),
        flow
      )

    upgradeResponse.flatMap { upgrade =>
      if (upgrade.response.status.isSuccess()) {
        Future.successful(LOGGER.info("WebSocket connected"))
      } else {
        throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
      }
    }

    promise
  }
}

object FinnhubClient {
  def apply(token: String): FinnhubClient = new FinnhubClient(token)

  lazy val SYMBOL_LOOKUP_ENDPOINT = "https://finnhub.io/api/v1/search"
  lazy val RECOMMENDATION_TRENDS_ENDPOINT = "https://finnhub.io/api/v1/stock/recommendation"
  lazy val WEBSOCKET_ENDPOINT = "wss://ws.finnhub.io"

  implicit val system: ActorSystem = ActorSystem()

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

  implicit val tradeDecoder: Decoder[Trade] = Decoder.instance {
    cursor =>
      for {
        data <- cursor.downField("data").as[List[Data]]
        typee <- cursor.get[String]("type")
      } yield Trade(typee, data)
  }

  implicit val dataDecoder: Decoder[Data] = Decoder.instance {
    cursor =>
      for {
        symbol <- cursor.get[String]("s")
        price <- cursor.get[Double]("p")
        timestamp <- cursor.get[Long]("t")
        volume <- cursor.get[Double]("v")
        conditions <- {
          val conditions = cursor.get[Int]("c")
          conditions match {
            case Right(c) => Right(c)
            case Left(_) => Right(0)
          }
        }
      } yield Data(symbol, price, timestamp, volume, conditions)
  }

  // Decoder set to ignore unknown fields
  implicit val unknownFields: Decoder[UnknownFieldSet] = Decoder.instance(
    cursor => Right(UnknownFieldSet())
  )

  implicit val recommendationTrendDecoder: Decoder[RecommendationTrend] = Decoder.forProduct7(
    "period",
    "strongBuy",
    "buy",
    "hold",
    "sell",
    "strongSell",
    "symbol")(RecommendationTrend.apply)
}


